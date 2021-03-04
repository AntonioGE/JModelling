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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.gui.display.EditorDisplayGL;

/**
 *
 * @author ANTONIO
 */
public class Navigate extends ObjectTool {

    public Navigate(View3D editor, ObjectMode objectMode) {
        super(editor, objectMode);
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
        editor.moveCamera(panel, e);
    }

    @Override
    public void mouseMoved(EditorDisplayGL panel, MouseEvent e) {

    }

    @Override
    public void keyTyped(EditorDisplayGL panel, KeyEvent e) {

    }

    @Override
    public void keyPressed(EditorDisplayGL panel, KeyEvent e) {
        mode.changeMode(e);
    }

    @Override
    public void keyReleased(EditorDisplayGL panel, KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(EditorDisplayGL panel, MouseWheelEvent e) {
        editor.zoomCamera(panel, e);
    }
    
}
