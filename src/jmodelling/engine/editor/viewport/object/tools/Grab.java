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

import com.jogamp.opengl.GLAutoDrawable;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;
import jmodelling.engine.editor.common.TypedFloat;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.object.hud.InfiniteLine;
import jmodelling.engine.transform.Transformation;
import jmodelling.gui.display.EditorDisplayGL;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Grab extends TransformTool {

    private static enum GrabType {
        PLANAR(new Vec3f(), new Vec3f()),
        X(new Vec3f(1.0f, 0.0f, 0.0f), new Vec3f(1.0f, 0.5f, 0.5f)),
        Y(new Vec3f(0.0f, 1.0f, 0.0f), new Vec3f(0.5f, 1.0f, 0.5f)),
        Z(new Vec3f(0.0f, 0.0f, 1.0f), new Vec3f(0.5f, 0.0f, 1.5f));

        public final Vec3f axis;
        public final Vec3f color;

        private GrabType(Vec3f axis, Vec3f color) {
            this.axis = axis;
            this.color = color;
        }
    }
    private GrabType grabType;

    public Grab(View3D editor, ObjectMode objectMode) {
        super(editor, objectMode);

        grabType = GrabType.PLANAR;

    }

    @Override
    public void init(GLAutoDrawable glad) {

    }

    @Override
    public void dispose(GLAutoDrawable glad) {

    }

    @Override
    public void display(GLAutoDrawable glad) {

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
                obj.loc.set(transforms.get(obj).loc);
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
        moveObjects();
        editor.repaintSameEditors();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (TypedFloat.isTypingAmount(e)) {
            transfAmount.processInput(e);
            moveObjects();
            editor.repaintSameEditors();
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE: {
                    exitTool();
                    editor.repaintSameEditors();
                    break;
                }

                case KeyEvent.VK_G: {
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
                    setLinearGrabType(GrabType.X);
                    moveObjects();
                    editor.repaintSameEditors();
                    break;
                }

                case KeyEvent.VK_Y: {
                    setLinearGrabType(GrabType.Y);
                    moveObjects();
                    editor.repaintSameEditors();
                    break;
                }

                case KeyEvent.VK_Z: {
                    setLinearGrabType(GrabType.Z);
                    moveObjects();
                    editor.repaintSameEditors();
                    break;
                }

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
        editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
    }

    private Vec3f linearTranslation() {
        return Transformation.linearTranslation_(transforms.get(lastSelected).loc, grabType.axis,
                Cam.pixelToView(firstMouseX, firstMouseY, editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                Cam.pixelToView(editor.getPanel().getMouseX(), editor.getPanel().getMouseY(), editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                editor.getTransf(), editor.getCam(), editor.getPanel().getAspect());
    }

    private Vec3f planarTranslation() {
        return Transformation.planarTranslation_(transforms.get(lastSelected).loc,
                Cam.pixelToView(firstMouseX, firstMouseY, editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                Cam.pixelToView(editor.getPanel().getMouseX(), editor.getPanel().getMouseY(), editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                editor.getCam(), editor.getPanel().getAspect());
    }

    private void moveObjects() {
        if (transfAmount.isEmpty()) {
            Vec3f trans;
            if (grabType.equals(GrabType.PLANAR)) {
                trans = planarTranslation();
            } else {
                trans = linearTranslation();
            }

            selectedObjs.forEach((obj) -> {
                obj.loc.set(transforms.get(obj).loc.add_(trans));
            });
        } else {
            try {
                final Vec3f trans = grabType.axis.scale_(transfAmount.getValue());
                selectedObjs.forEach((obj) -> {
                    obj.loc.set(transforms.get(obj).loc.add_(trans));
                });
            } catch (NumberFormatException ex) {

            }
        }
    }

    private void setLinearGrabType(GrabType grabLinear) {
        if (grabType != grabLinear) {
            grabType = grabLinear;
            editor.getScene().replaceHudObject(new InfiniteLine(
                    transforms.get(lastSelected).loc,
                    grabType.axis, grabType.color));
        } else {
            grabType = GrabType.PLANAR;
            editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
        }
    }

    @Override
    public void exitTool() {
        mode.setDefaultTool();
        editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
        editor.getPanel().setCursor(Cursor.getDefaultCursor());
    }

}
