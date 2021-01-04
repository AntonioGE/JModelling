/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.display;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
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

    private static final float axisCoords[] = new float[]{
        0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
    };

    private static final float axisColors[] = new float[]{
        1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };

    private float lastMouseX, lastMouseY;
    private Vec3f camPos = new Vec3f(-6.59f, 2.5f, 3.45f);
    //private Vec3f camPos = new Vec3f(0.0f, 0.0f, 10.0f);
    private Vec3f camTar = new Vec3f(0.0f, 0.0f, 0.0f);
    private Vec3f camUp = new Vec3f(0.0f, 0.0f, 1.0f);
    //private Vec3f camAngles = new Vec3f(63.0f, 0.0f, -110.0f);
    private Vec3f camAngles = camDirToAngles();
    //private Vec3f camAngles = new Vec3f(0.0f, 0.0f, 0.0f);

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

        Mat4f p = TransfMat.perspective_(60.0f, (float) getWidth() / getHeight(), 0.1f, 100.0f);
        Mat4f rx = TransfMat.rotation_(-camAngles.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotation_(-camAngles.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotation_(-camAngles.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f t = TransfMat.translation_(camPos.negate_());
        //Mat4f lookAt = TransfMat.lookAt_(camPos, camTar, camUp);
        Mat4f lookAt = TransfMat.lookAt_(new Vec3f(), new Vec3f(), camUp);

        gl.glLoadIdentity();
        gl.glMultMatrixf(p.toArray(), 0);
        //gl.glMultMatrixf(lookAt.toArray(), 0);

        gl.glMultMatrixf(rx.toArray(), 0);
        gl.glMultMatrixf(ry.toArray(), 0);
        gl.glMultMatrixf(rz.toArray(), 0);

        gl.glMultMatrixf(t.toArray(), 0);

        //camPos.print();
        //camPos.print();
        //camAngles.print();
        /*
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluPerspective(60.0f, (float)getWidth()/getHeight(), 0.1f, 100.0f);
        glu.gluLookAt(
                5.0f, 5.0f, 10.0f, 
                0.0f, 0.0f, 0.0f, 
                0.0f, 1.0f, 0.0f);*/
 /*gl.glTranslatef(0.0f, 0.0f, -20.0f);
        gl.glRotatef(45.0f, 1.0f, 0.0f, 0.0f);*/
        float[] matrix = new float[16];
        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, matrix, 0);

        //new Mat4f(matrix).print();
        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0, c = 0; i < 6; i++) {
            gl.glColor3fv(cubeColors, i * 3);
            for (int j = 0; j < 4; j++, c += 3) {
                gl.glVertex3fv(cubeCoords, c);
            }
        }
        gl.glEnd();

        gl.glLineStipple(1, (short) 0xF0F0);
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        gl.glScalef(20.0f, 20.0f, 20.0f);
        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < 6; i++) {
            gl.glColor3fv(axisColors, i * 3);
            gl.glVertex3fv(axisCoords, i * 3);
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
        lastMouseX = e.getX();
        lastMouseY = e.getY();
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
        float sensitivity = 2.0f;
        float deltaX = (e.getX() - lastMouseX) / sensitivity;
        float deltaY = (e.getY() - lastMouseY) / sensitivity;
        lastMouseX = e.getX();
        lastMouseY = e.getY();

        camAngles.add(new Vec3f(-deltaY, 0.0f, -deltaX));

        Vec3f anglesRotated = camAngles.clone();
        anglesRotated.x += 90.0f;
        anglesRotated.z -= 90.0f;
        Vec3f camDir = anglesRotated.anglesXZDegToVector_();
        camDir.z = -camDir.z;

        camPos = camTar.add_(camDir.negate()).scale(camPos.sub_(camTar).norm());

        //camAngles.print("CamAngles");
        //camDir.print("CamDir");
        //camPos = new Vec3f(0.0f, 0.0f, 1.0f).scale(camPos.norm()).

        /*
        camPos.rotateAroundDeg(camTar, camUp, deltaX);
        //Vec3f camDir = camTar.sub_(camPos);
        //camAngles = camDir.anglesXZ().add(new Vec3f(0, 0, 90.0f));
        
        
        Vec3f camDir = camTar.sub_(camPos);
        float norm = camDir.norm();
        camDir.normalize();
        Vec3f angles = camDir.anglesXZDeg_();
        angles.x += deltaY;
        camDir = angles.anglesXZDegToVector().negate();
        camPos = camTar.add_(camDir.scale(norm));
        camAngles = camDirToAngles();
        //angles.add(new Vec3f(90.0f, 0.0f, -90.0f));
        camAngles.print();
         */
 /*
        Vec3f camDir = camTar.sub_(camPos).normalize();
        Vec3f camRight = camDir.cross_(camUp).normalize();
        camPos.rotateAroundDeg(camTar, camRight, deltaY);
        camAngles = camDirToAngles();
         */
 /*
        Vec3f xRotAxis = camUp.clone();
        camPos.rotateAround(camTar, xRotAxis, deltaX);

        Vec3f camDirection = camPos.sub_(camTar).normalize();
        Vec3f camRight = camUp.cross_(camDirection).normalize();
        Vec3f yRotAxis = camRight.clone();
        camPos.rotateAround(camTar, yRotAxis, deltaY);
         */
        repaint();
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
        float delta = 1.2f;
        if (e.getWheelRotation() > 0) {
            camPos.sub(camTar);
            camPos.scale(delta);
            camPos.add(camTar);
        } else {
            camPos.sub(camTar);
            camPos.scale(1.0f / delta);
            camPos.add(camTar);
        }

        repaint();
    }

    //TODO: Move to camera class
    private Vec3f camDirToAngles() {
        Vec3f angles = camTar.sub_(camPos).anglesXZDeg();
        angles.add(new Vec3f(90.0f, 0.0f, -90.0f));
        return angles;
    }

}
