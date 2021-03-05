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
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.camera.CamArcball;
import jmodelling.gui.display.EditorDisplayGL;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class View3D extends Editor {

    protected Mode mode;
    protected CamArcball cam;

    protected int lastPressX, lastPressY;
    protected int mouseX, mouseY;

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

        gl.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_BLEND);

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

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        lighting(gl);

        Mat4f p = TransfMat.perspective_(cam.fov, panel.getAspect(), 0.1f, 1000.0f);
        Mat4f rx = TransfMat.rotationDeg_(-cam.rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotationDeg_(-cam.rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotationDeg_(-cam.rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f t = TransfMat.translation_(cam.loc.negate_());
        transf = p.mul_(rx).mul(ry).mul(rz).mul(t);

        gl.glLoadMatrixf(transf.toArray(), 0);

        engine.scene.updateGL(gl);
        engine.scene.getObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        gl.glDisable(GL2.GL_LIGHTING);
        engine.scene.getHudObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        mode.display(glad);
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
        mode.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mode.keyReleased(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        mode.mouseWheelMoved(e);
    }

    private void lighting(GL2 gl) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);

        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{1.0f, 1.0f, 0.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.3f, 0.3f, 0.3f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{0.1f, 0.1f, 0.1f, 0.0f}, 0);

        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[]{0.5f, 0.5f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[]{-1.0f, -1.0f, 0.0f, 0.0f}, 0);

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
        if (SwingUtilities.isLeftMouseButton(e)) {
            final float deltaX = -(float) (e.getX() - lastPressX) / (panel.getWidth() / 2);
            final float deltaY = (float) (e.getY() - lastPressY) / (panel.getHeight() / 2);
            lastPressX = e.getX();
            lastPressY = e.getY();

            Vec3f trans = new Vec3f(
                    deltaX * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)) * panel.getAspect(),
                    deltaY * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)),
                    0.0f
            );
            cam.loc.add(trans.mul(cam.getLocalAxis3f()));

        } else if (SwingUtilities.isRightMouseButton(e)) {
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

    @Override
    public String getEditorName() {
        return "VIEW3D";
    }

}
