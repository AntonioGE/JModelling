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

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.transform.Transform;
import jmodelling.engine.scene.Scene;

/**
 *
 * @author ANTONIO
 */
public abstract class TransformTool extends ObjectTool {

    protected final int firstMouseX, firstMouseY;
    protected HashMap<Object3D, Transform> transforms;
    protected final Set<Object3D> selectedObjs;
    protected final Object3D lastSelected;
    //private Vec3f center; //TODO: Move objects from the mean center
    protected String moveAmount;
    protected boolean negateMove;

    public TransformTool(View3D editor, ObjectMode objectMode) {
        super(editor, objectMode);

        firstMouseX = editor.getMouseX();
        firstMouseY = editor.getMouseY();

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

        moveAmount = "";
        negateMove = false;
    }

    protected boolean isTypingAmount(KeyEvent e) {
        return ("0123456789.-".contains(Character.toString(e.getKeyChar()))
                || e.getKeyCode() == KeyEvent.VK_DELETE)
                || (e.getKeyCode() == KeyEvent.VK_BACK_SLASH);
    }

    protected void parseAmount(KeyEvent e) {
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
