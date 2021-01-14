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

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.SwingUtilities;
import jmodelling.engine.object.camera.CamArcball;
import jmodelling.engine.object.mesh.MeshEditable;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.object.mesh.generator.Cube;
import jmodelling.engine.object.mesh.generator.EmptyMesh;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.engine.object.other.Axis;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
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
    private CamArcball cam = new CamArcball("",
            new Vec3f(5.99f, -6.7f, 3.85f),
            new Vec3f(0.0f, 0.0f, 0.0f),
            CamArcball.Type.PERSPECTIVE,
            0.1f,
            1000f,
            60.0f,
            1.0f
    );

    private Axis axis = new Axis("",
            new Vec3f(0.5f, 0.0f, 0.5f),
            new Vec3f(21.0f, 33.0f, 33.0f),
            new Vec3f(1.0f, 1.0f, 1.0f)
    );

    private final int w = 10, h = 100;
    private Axis[] cosas = new Axis[w * h];

    private MeshObject cube;

    public DisplayGL() {
        super(generateCapabilities());

        for (int i = 0, c = 0; i < w; i++) {
            for (int j = 0; j < h; j++, c++) {
                cosas[c] = new Axis("",
                        new Vec3f(i, j, (float) Math.random()),
                        new Vec3f((float) Math.random() * 360.0f, (float) Math.random() * 360.0f, (float) Math.random() * 360.0f),
                        new Vec3f(1.0f, 1.0f, 1.0f));
            }
        }

        cube = new EmptyMesh(new Cube().toMesh());

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

        gl.glEnable(GL2.GL_POLYGON_SMOOTH);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glEnable(GL2.GL_DEPTH_TEST);

        Mat4f p = TransfMat.perspective_(cam.fov, (float) getWidth() / getHeight(), 0.1f, 1000.0f);
        Mat4f rx = TransfMat.rotation_(-cam.rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotation_(-cam.rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotation_(-cam.rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f t = TransfMat.translation_(cam.loc.negate_());

        gl.glLoadIdentity();

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        //float[] ambientLight = {1f, 1f, 1f, 0f};
        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
        //float[] specularLight = {1f, 1f, 1f, 0f};
        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);
        float[] diffuseLight = {1f, 1f, 1f, 0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);

        //float[] emissionLight = {1f, 1f, 1f, 0f};
        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_EMISSION, emissionLight, 0);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{0.0f, 1.0f, 0.0f, 0.0f}, 0);

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glLoadIdentity();
        gl.glMultMatrixf(p.toArray(), 0);
        gl.glMultMatrixf(rx.toArray(), 0);
        gl.glMultMatrixf(ry.toArray(), 0);
        gl.glMultMatrixf(rz.toArray(), 0);
        gl.glMultMatrixf(t.toArray(), 0);

        gl.glColor4f(0.0f, 1.0f, 1.0f, 1.0f);

        cube.renderOpaque(gl);

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, -0.5f, -0.5f);

        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0, c = 0; i < 6; i++) {
            gl.glColor3fv(cubeColors, i * 3);
            for (int j = 0; j < 4; j++, c += 3) {
                gl.glVertex3fv(cubeCoords, c);
            }
        }
        gl.glEnd();
        gl.glPopMatrix();

        axis.renderOpaque(gl);

        for (Axis axis : cosas) {
            axis.renderOpaque(gl);
        }

        gl.glLineStipple(1, (short) 0xF0F0);
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        gl.glScalef(20.0f, 20.0f, 20.0f);
        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < 6; i++) {
            gl.glColor3fv(axisColors, i * 3);
            gl.glVertex3fv(axisCoords, i * 3);
        }
        gl.glEnd();

        gl.glLineStipple(1, (short) 0xFFFF);

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
        if (SwingUtilities.isLeftMouseButton(e)) {
            final float deltaX = -(float) (e.getX() - lastMouseX) / (getWidth() / 2);
            final float deltaY = (float) (e.getY() - lastMouseY) / (getHeight() / 2);
            lastMouseX = e.getX();
            lastMouseY = e.getY();

            final float aspect = (float) getWidth() / getHeight();
            Vec3f trans = new Vec3f(
                    deltaX * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)) * aspect,
                    deltaY * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)),
                    0.0f
            );
            cam.loc.add(trans.mul(cam.getLocalAxis3f()));

        } else if (SwingUtilities.isRightMouseButton(e)) {
            float sensitivity = 2.0f;
            float deltaX = (e.getX() - lastMouseX) / sensitivity;
            float deltaY = (e.getY() - lastMouseY) / sensitivity;
            lastMouseX = e.getX();
            lastMouseY = e.getY();

            cam.orbit(new Vec3f(-deltaY, 0.0f, -deltaX));
        }

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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float delta = 1.2f;
        if (e.getWheelRotation() > 0) {
            //cam.distToTarget *= delta;
            cam.moveTowardsTarget(cam.distToTarget * delta);
        } else {
            //cam.distToTarget /= delta;
            cam.moveTowardsTarget(cam.distToTarget / delta);
        }
        repaint();
    }

    /*
    //TODO: Move to camera class
    private Vec3f camDirToAngles() {
        Vec3f angles = camTar.sub_(camPos).anglesXZDeg();
        angles.add(new Vec3f(90.0f, 0.0f, -90.0f));
        return angles;
    }*/
    //TODO: This should be put into a custom OpenGL panel initialization code
    private static GLCapabilities generateCapabilities() {
        final GLProfile gp = GLProfile.get(GLProfile.GL2);
        GLCapabilities cap = new GLCapabilities(gp);
        cap.setSampleBuffers(true);
        cap.setNumSamples(8);
        return cap;
    }

}
