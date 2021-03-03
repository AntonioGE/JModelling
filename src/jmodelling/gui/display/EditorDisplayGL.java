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
package jmodelling.gui.display;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLSharedContextSetter;
import com.jogamp.opengl.awt.GLJPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import jmodelling.engine.editor.Editor;

/**
 *
 * @author ANTONIO
 */
public class EditorDisplayGL extends GLJPanel implements GLEventListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    private Editor editor;
    
    public EditorDisplayGL(GLAutoDrawable sharedDrawable, Editor editor) {
        this.editor = editor;
        
        setSharedAutoDrawable(sharedDrawable);
        
        addGLEventListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);

        setFocusable(true);
    }

    @Override
    public void init(GLAutoDrawable glad) {
        editor.init(this, glad);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        editor.dispose(this, glad);
    }

    @Override
    public void display(GLAutoDrawable glad) {
        editor.display(this, glad);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int width, int height) {
        editor.reshape(this, glad, i, i1, width, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        editor.mouseClicked(this, e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        editor.mousePressed(this, e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        editor.mouseReleased(this, e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        editor.mouseEntered(this, e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        editor.mouseExited(this, e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        editor.mouseDragged(this, e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        editor.mouseMoved(this, e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        editor.keyTyped(this, e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        editor.keyPressed(this, e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        editor.keyReleased(this, e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        editor.mouseWheelMoved(this, e);
    }
    
    public float getAspect(){
        return (float)getWidth() / getHeight();
    }

}
