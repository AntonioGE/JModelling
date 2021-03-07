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
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Scale extends TransformTool {

    private static enum ScaleType {
        FULL(new Vec3f(), new Vec3f(), Cursor.getDefaultCursor(), true, true, true),
        X(new Vec3f(1.0f, 0.0f, 0.0f), new Vec3f(1.0f, 0.5f, 0.5f), Cursor.getDefaultCursor(), true, false, false),
        Y(new Vec3f(0.0f, 1.0f, 0.0f), new Vec3f(0.5f, 1.0f, 0.5f), Cursor.getDefaultCursor(), false, true, false),
        Z(new Vec3f(0.0f, 0.0f, 1.0f), new Vec3f(0.5f, 0.0f, 1.5f), Cursor.getDefaultCursor(), false, false, true);

        public final boolean x;
        public final boolean y;
        public final boolean z;
        public final Vec3f axis;
        public final Vec3f color;
        public final Cursor cursor;

        private ScaleType(Vec3f axis, Vec3f color, Cursor cursor, boolean x, boolean y, boolean z) {
            this.axis = axis;
            this.color = color;
            this.cursor = cursor;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    private ScaleType scalingType;

    public Scale(View3D editor, ObjectMode objectMode) {
        super(editor, objectMode);

        scalingType = ScaleType.FULL;
    }

    @Override
    public void init(GLAutoDrawable glad) {
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        Vec2f center = Transformation.worldToView_(transforms.get(lastSelected).loc, editor.getTransf());
        Vec2f cursor = Cam.pixelToView(editor.getPanel().getMouseX(), editor.getPanel().getMouseY(), editor.getPanel().getWidth(), editor.getPanel().getHeight());

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
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
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            exitTool();
            editor.repaintSameEditors();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            selectedObjs.forEach((obj) -> {
                obj.sca.set(transforms.get(obj).sca);
            });
            exitTool();
            editor.repaintSameEditors();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        scaleObjects();
        editor.repaintSameEditors();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                exitTool();
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_S: {
                exitTool();
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_ENTER: {
                exitTool();
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_X: {
                setScaleType(ScaleType.X);
                scaleObjects();
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_Y: {
                setScaleType(ScaleType.Y);
                scaleObjects();
                editor.repaintSameEditors();
                break;
            }

            case KeyEvent.VK_Z: {
                setScaleType(ScaleType.Z);
                scaleObjects();
                editor.repaintSameEditors();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void exitTool() {
        mode.setDefaultTool();
        editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
        editor.getPanel().setCursor(Cursor.getDefaultCursor());
    }

    private Vec3f fullScaling() {
        return Transformation.scale_(transforms.get(lastSelected).loc,
                scalingType.x, scalingType.y, scalingType.z,
                Cam.pixelToView(firstMouseX, firstMouseY, editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                Cam.pixelToView(editor.getPanel().getMouseX(), editor.getPanel().getMouseY(), editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                editor.getTransf(), editor.getPanel().getAspect());
    }

    private Vec3f axisScaling() {
        return Transformation.scale_(transforms.get(lastSelected).loc,
                scalingType.x, scalingType.y, scalingType.z,
                Cam.pixelToView(firstMouseX, firstMouseY, editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                Cam.pixelToView(editor.getPanel().getMouseX(), editor.getPanel().getMouseY(), editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                editor.getTransf(), editor.getPanel().getAspect());
    }

    private void scaleObjects() {
        if (moveAmount.isEmpty()) {
            Vec3f sca;
            switch (scalingType) {
                case FULL:
                    sca = fullScaling();
                    break;
                default:
                    sca = axisScaling();
                    break;
            }

            selectedObjs.forEach((obj) -> {
                //TODO: rotate scaling?
                obj.sca.set(transforms.get(obj).sca.had_(sca));
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

    private void setScaleType(ScaleType newType) {
        scalingType = newType;
        editor.getPanel().setCursor(scalingType.cursor);
    }

}
