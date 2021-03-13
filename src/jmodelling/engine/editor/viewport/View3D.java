/*
 * MIT License
 * 
 * Copyright (c) 2021 Antonio GE
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jmodelling.engine.editor.viewport;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;
import jmodelling.engine.Engine;
import jmodelling.engine.editor.Editor;
import jmodelling.engine.editor.viewport.edit.EditMode;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.camera.CamArcball;
import jmodelling.engine.object.mesh.MeshEditableObject;
import jmodelling.gui.display.EditorDisplayGL;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class View3D extends Editor {

    public static final String NAME = "VIEW3D";

    protected Mode mode;
    protected CamArcball cam;

    protected int lastPressX, lastPressY;
    protected int mouseX, mouseY;

    protected boolean shiftPressed;

    protected Mat4f transf;

    public View3D(Engine engine) {
        super(engine);

        cam = new CamArcball("",
                new Vec3f(0.0f, -5.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f),
                CamArcball.Type.PERSPECTIVE, 0.1f, 1000f, 60.0f, 1.0f);

        mode = new ObjectMode(this, engine);
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        gl.glClearColor(0.2235f, 0.2235f, 0.2235f, 1.0f);

        //gl.glEnable(GL2.GL_MULTISAMPLE);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_RESCALE_NORMAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_DIFFUSE);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        gl.glDepthFunc(GL2.GL_LESS);

        engine.scene.updateGL(gl);

        mode.init(glad);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        mode.dispose(glad);
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);//| GL2.GL_STENCIL_BUFFER_BIT);

        gl.glLoadIdentity();
        lighting(gl);

        Mat4f p = cam.getProjectionMatrix(panel.getAspect());
        Mat4f mv = cam.getModelViewMatrix();
        transf = p.mul_(mv);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadMatrixf(p.toArray(), 0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadMatrixf(mv.toArray(), 0);

        mode.display(glad);

        if (engine.scene.getLastSelectedObject() != null) {
            engine.textRenderer.beginRendering(panel.getWidth(), panel.getHeight());
            engine.textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            engine.textRenderer.draw(
                    "(" + String.valueOf(engine.scene.getSelectedObjects().size()) + ") "
                    + engine.scene.getLastSelectedObject().name,
                    60, 10);
            engine.textRenderer.endRendering();
        }

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        final int sizePixels = 25;
        final int xOffsetPixels = 25;
        final int yOffsetPixels = 25;
        final float scale = (float) sizePixels / panel.getWidth() * 2;
        final float xOffset = ((float) xOffsetPixels / panel.getWidth()) * 2;
        final float yOffset = ((float) yOffsetPixels / panel.getHeight()) * 2;
        gl.glTranslatef(xOffset - 1.0f, yOffset - 1.0f, 0.0f);
        gl.glScalef(scale, scale, scale);

        gl.glScalef(1.0f, panel.getAspect(), 1.0f);

        gl.glLineWidth(2.0f);

        gl.glRotatef(-cam.rot.x, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(-cam.rot.y, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-cam.rot.z, 0.0f, 0.0f, 1.0f);

        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(1.0f, 0.0f, 0.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);

        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);

        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);

        gl.glEnd();

        gl.glLineWidth(1.0f);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPopMatrix();

    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int width, int height) {
        mode.reshape(glad, i, i1, width, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mode.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastPressX = e.getX();
        lastPressY = e.getY();

        mode.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mode.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mode.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mode.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mode.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        mode.mouseMoved(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        mode.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            /*
            case KeyEvent.VK_TAB: {
                setModesInEditors(toggleMode(mode));
                repaintSameEditors();
                break;
            }*/
            case KeyEvent.VK_SHIFT: {
                shiftPressed = true;
                break;
            }
        }
        mode.keyPressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT: {
                shiftPressed = false;
                break;
            }
        }
        mode.keyReleased(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        mode.mouseWheelMoved(e);
    }

    private void lighting(GL2 gl) {
        gl.glEnable(GL2.GL_LIGHTING);
        //gl.glEnable (GL2.GL_COLOR_MATERIAL ) ;
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);
        //gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, new float[]{1.0f, 0.0f, 1.0f, 0.0f}, 0);

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 0.0f}, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 55.0f);

        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{0.7f, 0.7f, 0.7f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{-1.0f, 1.0f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.3f, 0.3f, 0.3f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);

        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[]{0.4f, 0.4f, 0.6f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[]{1.0f, -1.0f, -1.0f, 0.0f}, 0);

    }

    public void zoomCamera(MouseWheelEvent e) {
        float delta = 1.2f;
        if (e.getWheelRotation() > 0) {
            cam.moveTowardsTarget(cam.distToTarget * delta);
        } else {
            cam.moveTowardsTarget(cam.distToTarget / delta);
        }
        panel.repaint();
    }

    public void moveCamera(MouseEvent e) {
        if (shiftPressed && (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e))) {
            final float deltaX = -(float) (e.getX() - lastPressX) / (panel.getWidth() / 2);
            final float deltaY = (float) (e.getY() - lastPressY) / (panel.getHeight() / 2);
            lastPressX = e.getX();
            lastPressY = e.getY();

            Vec3f trans = new Vec3f(
                    deltaX * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)) * panel.getAspect(),
                    deltaY * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)),
                    0.0f
            );
            cam.loc.add(trans.mul(cam.getRotationMatrix3f()));

        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            float sensitivity = 2.0f;
            float deltaX = (e.getX() - lastPressX) / sensitivity;
            float deltaY = (e.getY() - lastPressY) / sensitivity;
            lastPressX = e.getX();
            lastPressY = e.getY();

            cam.orbit(new Vec3f(-deltaY, 0.0f, -deltaX));
        }
        panel.repaint();
    }

    //TODO: Move all of this to the panel class?
    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getLastPressX() {
        return lastPressX;
    }

    public int getLastPressY() {
        return lastPressY;
    }

    public CamArcball getCam() {
        return cam;
    }

    public Mat4f getTransf() {
        return transf;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    public void changeMode(Mode newMode) {
        mode.destroy();
        mode = newMode;
    }

    //TODO: this code needs refactoring
    public void setModesInEditors(String modeName) {
        switch (modeName) {
            case EditMode.NAME: {
                engine.scene.editSelectedObject();
                Object3D obj = engine.scene.getLastSelectedObject();
                if (obj != null) {
                    MeshEditableObject emeshObj = (MeshEditableObject) (obj);
                    for (EditorDisplayGL panel : engine.getEditorDisplays()) {
                        if (panel.getEditor().getName().equals(View3D.NAME)) {
                            View3D editor = (View3D) panel.getEditor();
                            editor.changeMode(new EditMode(editor, engine, emeshObj));
                        }
                    }
                }
                break;
            }
            case ObjectMode.NAME: {
                for (EditorDisplayGL panel : engine.getEditorDisplays()) {
                    if (panel.getEditor().getName().equals(View3D.NAME)) {
                        View3D editor = (View3D) panel.getEditor();
                        editor.changeMode(new ObjectMode(editor, engine));
                    }
                }
                break;
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

}
