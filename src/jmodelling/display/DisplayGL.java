/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.display;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class DisplayGL extends GLJPanel implements GLEventListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    private static final float cubeCoords[] = new float[]{
        1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
        1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };

    private static final float cubeColors[] = new float[]{
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 1.0f,};

    public DisplayGL() {
        addGLEventListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);

        setFocusable(true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glEnable(GL2.GL_DEPTH_TEST);

        Mat4f p = TransfMat.perspective_(60.0f, (float)getWidth()/getHeight(), 0.1f, 100.0f);
        Mat4f r = TransfMat.rotation_(45.0f, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f t = TransfMat.translation_(new Vec3f(0.0f, 0.0f, -20.0f));
        
        p.print();
        r.print();
        t.print();
        
        gl.glLoadIdentity();
        //gl.glMultMatrixf(p.toArray(), 0);
        //gl.glMultMatrixf(r.toArray(), 0);
        gl.glMultMatrixf(t.toArray(), 0);
        
        
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -20.0f);
        //gl.glRotatef(45.0f, 1.0f, 0.0f, 0.0f);

        float[] matrix = new float[16]; 
        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, matrix, 0); 
        
        new Mat4f(matrix).print();
       
        
        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0, c = 0; i < 6; i++) {
            gl.glColor3fv(cubeColors, i * 3);
            for (int j = 0; j < 4; j++, c += 3) {
                gl.glVertex3fv(cubeCoords, c);
            }
        }
        gl.glEnd();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

}
