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

    protected EditorDisplayGL panel;
    
    public Tool(EditorDisplayGL panel){
        this.panel = panel;
    }
    
    public abstract void init(GLAutoDrawable glad);

    public abstract void dispose(GLAutoDrawable glad);

    public abstract void display(GLAutoDrawable glad);

    public abstract void reshape(GLAutoDrawable glad, int x, int y, int width, int height);

    public abstract void mouseClicked(MouseEvent e);

    public abstract void mousePressed(MouseEvent e);

    public abstract void mouseReleased(MouseEvent e);

    public abstract void mouseEntered(MouseEvent e);

    public abstract void mouseExited(MouseEvent e);

    public abstract void mouseDragged(MouseEvent e);

    public abstract void mouseMoved(MouseEvent e);

    public abstract void keyTyped(KeyEvent e);

    public abstract void keyPressed(KeyEvent e);

    public abstract void keyReleased(KeyEvent e);

    public abstract void mouseWheelMoved(MouseWheelEvent e);

    public abstract void destroy();

    public abstract void exitTool(EditorDisplayGL panel);
}
