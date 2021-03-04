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
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.object.hud.Axis;
import jmodelling.engine.object.hud.InfiniteLine;
import jmodelling.engine.object.transform.Transform;
import jmodelling.engine.scene.Scene;
import jmodelling.engine.transform.Transformation;
import jmodelling.gui.display.EditorDisplayGL;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Grab extends ObjectTool {

    private final int lastGrabX, lastGrabY;

    private HashMap<Object3D, Transform> transforms;
    private final HashSet<Object3D> selectedObjs;
    private final Object3D lastSelected;
    //private Vec3f center; //TODO: Move objects from the mean center

    private String moveAmount;
    private boolean negateMove;

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

        lastGrabX = editor.getMouseX();
        lastGrabY = editor.getMouseY();

        Scene scene = editor.getScene();
        selectedObjs = scene.getSelectedObjects();
        transforms = new HashMap<>(selectedObjs.size());
        selectedObjs.forEach((obj) -> {
            transforms.put(obj, obj.getTransform().clone());
        });

        if (scene.isLastObjectSelected()) {
            lastSelected = scene.getLastSelectedObject();
        } else {
            lastSelected = selectedObjs.iterator().next();
        }

        grabType = GrabType.PLANAR;

        moveAmount = "";
        negateMove = false;
    }

    @Override
    public void init(EditorDisplayGL panel, GLAutoDrawable glad) {

    }

    @Override
    public void dispose(EditorDisplayGL panel, GLAutoDrawable glad) {

    }

    @Override
    public void display(EditorDisplayGL panel, GLAutoDrawable glad) {

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
            exitMode();
            panel.repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            selectedObjs.forEach((obj) -> {
                obj.loc.set(transforms.get(obj).loc);
            });
            exitMode();
            panel.repaint();
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
        moveObjects(panel);
        panel.repaint();
    }

    @Override
    public void keyTyped(EditorDisplayGL panel, KeyEvent e) {

    }

    @Override
    public void keyPressed(EditorDisplayGL panel, KeyEvent e) {
        if (isTypingAmount(e)){
            parseAmount(e);
            moveObjects(panel);
            panel.repaint();
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE: {
                    exitMode();
                    panel.repaint();
                    break;
                }

                case KeyEvent.VK_G: {
                    exitMode();
                    panel.repaint();
                    break;
                }
                
                case KeyEvent.VK_ENTER:{
                    exitMode();
                    panel.repaint();
                    break;
                }

                case KeyEvent.VK_X: {
                    setLinearGrabType(GrabType.X);
                    moveObjects(panel);
                    panel.repaint();
                    break;
                }

                case KeyEvent.VK_Y: {
                    setLinearGrabType(GrabType.Y);
                    moveObjects(panel);
                    panel.repaint();
                    break;
                }

                case KeyEvent.VK_Z: {
                    setLinearGrabType(GrabType.Z);
                    moveObjects(panel);
                    panel.repaint();
                    break;
                }

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

    private Vec3f linearTranslation(EditorDisplayGL panel) {
        return Transformation.linearTranslation_(transforms.get(lastSelected).loc, grabType.axis,
                Cam.pixelToView(lastGrabX, lastGrabY, panel.getWidth(), panel.getHeight()),
                Cam.pixelToView(panel.getMouseX(), panel.getMouseY(), panel.getWidth(), panel.getHeight()),
                editor.getTransf(), editor.getCam(), panel.getAspect());
    }

    private Vec3f planarTranslation(EditorDisplayGL panel) {
        return Transformation.planarTranslation_(transforms.get(lastSelected).loc,
                Cam.pixelToView(lastGrabX, lastGrabY, panel.getWidth(), panel.getHeight()),
                Cam.pixelToView(panel.getMouseX(), panel.getMouseY(), panel.getWidth(), panel.getHeight()),
                editor.getCam(), panel.getAspect());
    }

    private void moveObjects(EditorDisplayGL panel) {
        if (moveAmount.isEmpty()) {
            Vec3f trans;
            if (grabType.equals(GrabType.PLANAR)) {
                trans = planarTranslation(panel);
            } else {
                trans = linearTranslation(panel);
            }

            selectedObjs.forEach((obj) -> {
                obj.loc.set(transforms.get(obj).loc.add_(trans));
            });
        } else {
            try {
                float module = Float.valueOf(moveAmount);
                if (negateMove) {
                    module = -module;
                }

                final Vec3f trans = grabType.axis.scale_(module);
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

    private void exitMode() {
        mode.setDefaultTool();
        editor.getScene().removeHudObject(InfiniteLine.TYPE_NAME);
    }

    private boolean isTypingAmount(KeyEvent e) {
        return ("0123456789.-".contains(Character.toString(e.getKeyChar()))
                || e.getKeyCode() == KeyEvent.VK_DELETE)
                || (e.getKeyCode() == KeyEvent.VK_BACK_SLASH);
    }

    private void parseAmount(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isDigit(c)) {
            moveAmount += c;
        } else if (c == '.') {
            moveAmount += c;
        } else if (c == '-') {
            negateMove = !negateMove;
        } else if ((e.getKeyCode() == KeyEvent.VK_DELETE) || (e.getKeyCode() == KeyEvent.VK_BACK_SLASH)) {
            if (moveAmount.length() > 0) {
                moveAmount = moveAmount.substring(0, moveAmount.length() - 1);
            }
        }
    }

}
