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
package jmodelling.engine.editor;

import com.jogamp.opengl.GLAutoDrawable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import jmodelling.gui.display.EditorDisplayGL;

/**
 *
 * @author ANTONIO
 */
public abstract class Tool {

    public abstract void init(EditorDisplayGL panel, GLAutoDrawable glad);

    public abstract void dispose(EditorDisplayGL panel, GLAutoDrawable glad);

    public abstract void display(EditorDisplayGL panel, GLAutoDrawable glad);

    public abstract void reshape(EditorDisplayGL panel, GLAutoDrawable glad, int x, int y, int width, int height);

    public abstract void mouseClicked(EditorDisplayGL panel, MouseEvent e);

    public abstract void mousePressed(EditorDisplayGL panel, MouseEvent e);

    public abstract void mouseReleased(EditorDisplayGL panel, MouseEvent e);

    public abstract void mouseEntered(EditorDisplayGL panel, MouseEvent e);

    public abstract void mouseExited(EditorDisplayGL panel, MouseEvent e);

    public abstract void mouseDragged(EditorDisplayGL panel, MouseEvent e);

    public abstract void mouseMoved(EditorDisplayGL panel, MouseEvent e);

    public abstract void keyTyped(EditorDisplayGL panel, KeyEvent e);

    public abstract void keyPressed(EditorDisplayGL panel, KeyEvent e);

    public abstract void keyReleased(EditorDisplayGL panel, KeyEvent e);

    public abstract void mouseWheelMoved(EditorDisplayGL panel, MouseWheelEvent e);

    public abstract void destroy();
}
