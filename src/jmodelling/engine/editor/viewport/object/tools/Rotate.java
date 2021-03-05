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
package jmodelling.engine.editor.viewport.object.tools;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.object.hud.InfiniteLine;
import jmodelling.engine.transform.Transformation;
import jmodelling.gui.display.EditorDisplayGL;
import jmodelling.math.mat.Mat3f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;


/**
 *
 * @author ANTONIO
 */
public class Rotate extends TransformTool {

    private static enum RotationType {
        PLANAR(new Vec3f(), new Vec3f(), Cursor.getDefaultCursor()),
        BALL(new Vec3f(), new Vec3f(), Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)),
        X(new Vec3f(1.0f, 0.0f, 0.0f), new Vec3f(1.0f, 0.5f, 0.5f), Cursor.getDefaultCursor()),
        Y(new Vec3f(0.0f, 1.0f, 0.0f), new Vec3f(0.5f, 1.0f, 0.5f), Cursor.getDefaultCursor()),
        Z(new Vec3f(0.0f, 0.0f, 1.0f), new Vec3f(0.5f, 0.0f, 1.5f), Cursor.getDefaultCursor());

        public final Vec3f axis;
        public final Vec3f color;
        public final Cursor cursor;

        private RotationType(Vec3f axis, Vec3f color, Cursor cursor) {
            this.axis = axis;
            this.color = color;
            this.cursor = cursor;
        }
    }
    private RotationType rotationType;
    private RotationType prevRotationType;

    public Rotate(View3D editor, ObjectMode objectMode) {
        super(editor, objectMode);

        rotationType = RotationType.PLANAR;
        prevRotationType = rotationType;
    }

    @Override
    public void init(EditorDisplayGL panel, GLAutoDrawable glad) {

    }

    @Override
    public void dispose(EditorDisplayGL panel, GLAutoDrawable glad) {

    }

    @Override
    public void display(EditorDisplayGL panel, GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        if (rotationType != RotationType.BALL) {
            Vec2f center = Transformation.worldToView_(transforms.get(lastSelected).loc, editor.getTransf());
            Vec2f cursor = Cam.pixelToView(panel.getMouseX(), panel.getMouseY(), panel.getWidth(), panel.getHeight());

            gl.glPushMatrix();

            gl.glLoadIdentity();

            gl.glDisable(GL2.GL_DEPTH_TEST);
            gl.glEnable(GL2.GL_LINE_STIPPLE);
            gl.glLineStipple(1, (short) 0xF0F0);

            gl.glBegin(GL2.GL_LINES);
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glVertex3f(center.x, center.y, 0.0f);
            gl.glVertex3f(cursor.x, cursor.y, 0.0f);
            gl.glEnd();

            gl.glDisable(GL2.GL_LINE_STIPPLE);
            gl.glEnable(GL2.GL_DEPTH_TEST);

            gl.glPopMatrix();
        }

    }

    @Override
    public void reshape(EditorDisplayGL panel, GLAutoDrawable glad, int x, int y, int width, int height) {

    }

    @Override
    public void mouseClicked(EditorDisplayGL panel, MouseEvent e) {

    }

    @Override
    public void mousePressed(EditorDisplayGL panel, MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            exitTool(panel);
            editor.repaintSameEditors();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            selectedObjs.forEach((obj) -> {
                obj.rot.set(transforms.get(obj).rot);
            });
            exitTool(panel);
            editor.repaintSameEditors();
        }
    }

    @Override
    public void mouseReleased(EditorDisplayGL panel, MouseEvent e) {

    }

    @Override
    public void mouseEntered(EditorDisplayGL panel, MouseEvent e) {

    }

    @Override
    public void mouseExited(EditorDisplayGL panel, MouseEvent e) {

    }

    @Override
    public void mouseDragged(EditorDisplayGL panel, MouseEvent e) {

    }

    @Override
    public void mouseMoved(EditorDisplayGL panel, MouseEvent e) {
        rotateObjects(panel);
        editor.repaintSameEditors();
    }

    @Override
    public void keyTyped(EditorDisplayGL panel, KeyEvent e) {

    }

    @Override
    public void keyPressed(EditorDisplayGL panel, KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                exitTool(panel);
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_R: {
                if (rotationType.equals(RotationType.BALL)) {
                    setRotationType(panel, prevRotationType);
                }else{
                    setRotationType(panel, RotationType.BALL);
                }
                rotateObjects(panel);
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_ENTER: {
                exitTool(panel);
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_X: {
                setAxisRotationType(panel, RotationType.X);
                rotateObjects(panel);
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_Y: {
                setAxisRotationType(panel, RotationType.Y);
                rotateObjects(panel);
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_Z: {
                setAxisRotationType(panel, RotationType.Z);
                rotateObjects(panel);
                editor.repaintSameEditors();
                break;
            }
        }
    }

    @Override
    public void keyReleased(EditorDisplayGL panel, KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(EditorDisplayGL panel, MouseWheelEvent e) {

    }

    @Override
    public void destroy() {
        editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
    }

    private Mat3f planarRotation(EditorDisplayGL panel) {
        return Transformation.planarRotation_(transforms.get(lastSelected).loc,
                Cam.pixelToView(firstMouseX, firstMouseY, panel.getWidth(), panel.getHeight()),
                Cam.pixelToView(panel.getMouseX(), panel.getMouseY(), panel.getWidth(), panel.getHeight()),
                editor.getTransf(), editor.getCam(), panel.getAspect());
    }

    private Mat3f axisRotation(EditorDisplayGL panel) {
        return Transformation.axisRotation_(transforms.get(lastSelected).loc,
                rotationType.axis,
                Cam.pixelToView(firstMouseX, firstMouseY, panel.getWidth(), panel.getHeight()),
                Cam.pixelToView(panel.getMouseX(), panel.getMouseY(), panel.getWidth(), panel.getHeight()),
                editor.getTransf(), editor.getCam(), panel.getAspect());
    }

    private Mat3f ballRotation(EditorDisplayGL panel) {
        return Transformation.ballRotation_(transforms.get(lastSelected).loc,
                Cam.pixelToView(firstMouseX, firstMouseY, panel.getWidth(), panel.getHeight()),
                Cam.pixelToView(panel.getMouseX(), panel.getMouseY(), panel.getWidth(), panel.getHeight()),
                editor.getTransf(), editor.getCam(), panel.getAspect(), 3.0f);
    }

    private void rotateObjects(EditorDisplayGL panel) {
        if (moveAmount.isEmpty()) {
            Mat3f rot;
            switch (rotationType) {
                case PLANAR:
                    rot = planarRotation(panel);
                    break;
                case BALL:
                    rot = ballRotation(panel);
                    break;
                default:
                    rot = axisRotation(panel);
                    break;
            }
            selectedObjs.forEach((obj) -> {
                Vec3f angles = TransfMat.matToEulerDeg_(rot.mul(TransfMat.eulerDegToMat_(transforms.get(obj).rot)));
                obj.rot.set(angles);
            });
        } else {
            try {
                float module = Float.valueOf(moveAmount);
                if (negateMove) {
                    module = -module;
                }

                //final Vec3f trans = grabType.axis.scale_(module);
                selectedObjs.forEach((obj) -> {
                    //obj.loc.set(transforms.get(obj).loc.add_(trans));
                });
            } catch (NumberFormatException ex) {

            }
        }

    }

    private void setAxisRotationType(EditorDisplayGL panel, RotationType rotationLinear) {
        if (rotationType != rotationLinear) {
            setRotationType(panel, rotationLinear);
            editor.getScene().replaceHudObject(new InfiniteLine(
                    transforms.get(lastSelected).loc,
                    rotationType.axis, rotationType.color));
        } else {
            setRotationType(panel, RotationType.PLANAR);
            editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
        }
    }

    private void setRotationType(EditorDisplayGL panel, RotationType newType) {
        prevRotationType = rotationType;
        rotationType = newType;
        panel.setCursor(rotationType.cursor);
    }

    @Override
    public void exitTool(EditorDisplayGL panel) {
        mode.setDefaultTool();
        editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
        panel.setCursor(Cursor.getDefaultCursor());
    }

}
