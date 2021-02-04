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
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.object.camera.CamArcball;
import jmodelling.engine.object.mesh.MeshEditable;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.object.mesh.generator.Cube;
import jmodelling.engine.object.mesh.generator.EmptyMesh;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.engine.object.other.Axis;
import jmodelling.engine.transform.Transform;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;
import jmodelling.math.vec.Vec4f;

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
            //new Vec3f(5.99f, -6.7f, 3.85f),
            new Vec3f(0.0f, -5.0f, 0.0f),
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

    private boolean grab = false;
    private int lastGrabX, lastGrabY;
    private int mouseX, mouseY;
    private Vec3f objPos = new Vec3f();
    private Vec3f objRot = new Vec3f();
    private float distToObj;
    private Mat4f transf;
    private Vec3f moveAxis;

    private ArrayList<Vec3f> points = new ArrayList<Vec3f>() {
        {

        }
    };

    private ArrayList<Vec3f> lines = new ArrayList<Vec3f>();

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

        gl.glEnable(GL2.GL_MULTISAMPLE);
        gl.glEnable(GL2.GL_POLYGON_SMOOTH);
        gl.glEnable(GL2.GL_POINT_SMOOTH);
        //gl.glEnable(GL2.GL_LINE_SMOOTH);

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
        Mat4f rx = TransfMat.rotationDeg_(-cam.rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotationDeg_(-cam.rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotationDeg_(-cam.rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f t = TransfMat.translation_(cam.loc.negate_());

        transf = p.mul_(rx).mul(ry).mul(rz).mul(t);

        //transf.print();
        //cam.getLocalAxis3f().print();
        gl.glEnable(GL2.GL_BLEND);

        gl.glLoadIdentity();
        gl.glMultMatrixf(p.toArray(), 0);
        gl.glMultMatrixf(rx.toArray(), 0);
        gl.glMultMatrixf(ry.toArray(), 0);
        gl.glMultMatrixf(rz.toArray(), 0);
        gl.glMultMatrixf(t.toArray(), 0);

        gl.glLoadMatrixf(transf.toArray(), 0);

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

        gl.glPointSize(4.0f);
        gl.glBegin(GL2.GL_POINTS);
        for (Vec3f point : points) {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glVertex3f(point.x, point.y, point.z);
        }
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < lines.size() / 2; i++) {
            Vec3f p1 = lines.get(i * 2);
            Vec3f p2 = lines.get(i * 2 + 1);
            gl.glColor3f(1.0f, 0.5f, 0.0f);
            gl.glVertex3f(p1.x, p1.y, p1.z);
            gl.glColor3f(1.0f, 0.5f, 0.0f);
            gl.glVertex3f(p2.x, p2.y, p2.z);
        }
        gl.glEnd();

        axis.renderOpaque(gl);

        for (Axis axis : cosas) {
            axis.renderOpaque(gl);
        }

        if (grab) {
            gl.glPushMatrix();
            gl.glTranslatef(objPos.x, objPos.y, objPos.z);
            objPos.print();
            final float inf = 10000f;
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(moveAxis.x * inf, moveAxis.y * inf, moveAxis.z * inf);
            gl.glVertex3f(-moveAxis.x * inf, -moveAxis.y * inf, -moveAxis.z * inf);
            gl.glEnd();
            gl.glPopMatrix();
        }

        gl.glLineStipple(1, (short) 0xF0F0);
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        gl.glPushMatrix();
        gl.glScalef(20.0f, 20.0f, 20.0f);
        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < 6; i++) {
            gl.glColor3fv(axisColors, i * 3);
            gl.glVertex3fv(axisCoords, i * 3);
        }
        gl.glEnd();
        gl.glPopMatrix();

        gl.glLineStipple(1, (short) 0xFFFF);

        //cam.getDir().print();
        //new Vec3f(0.0f, 0.0f, -1.0f).mul(cam.getLocalAxis3f()).print();
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

        if (SwingUtilities.isLeftMouseButton(e)) {
            cam.viewPosToRay(e.getX(), e.getY(), getWidth(), getHeight()).print("Ray");
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
        mouseX = e.getX();
        mouseY = e.getY();

        if (grab) {

            /*
            Vec3f trans = Transform.linearTranslation(objPos, moveAxis,
                    Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight()),
                    Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight()),
                    transf, cam, (float) getWidth() / getHeight());
            */
            
            /*
            Vec3f trans = Transform.planarTranslation(objPos,
                    Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight()),
                    Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight()), 
                    cam, (float) getWidth() / getHeight());
            
            cube.loc.set(objPos.add_(trans));*/

            Vec3f rot = Transform.planarRotation(objPos, 
                    Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight()),
                    Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight()), 
                    transf, cam, (float) getWidth() / getHeight());
            
            cube.rot.set(objRot.add_(rot.toDegrees()));
            
            /*
            Vec4f p1 = new Vec4f(objPos.add_(moveAxis), 1.0f);
            Vec4f p2 = new Vec4f(objPos, 1.0f);
            p1.mul(transf);
            p2.mul(transf);
            p1.scale(1.0f / p1.w);
            p2.scale(1.0f / p2.w);
            Vec2f axis2D = new Vec2f(p1.x - p2.x, p1.y - p2.y);
            axis2D.x *= (float) getWidth() / getHeight();
            axis2D.normalize();

            Vec2f o2d = new Vec2f(p2.x * (float) getWidth() / getHeight(), p2.y);

            axis2D.print("axis 4D");

            Vec2f o = Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight());
            Vec2f p = Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight());

            float proyO = o.sub_(o2d).dot(axis2D);
            Vec2f q = o2d.add_(axis2D.scale_(proyO));

            float proyP = p.sub_(o2d).dot(axis2D);
            Vec2f r = o2d.add_(axis2D.scale_(proyP));

            Vec3f a = cam.viewPosToRay(q);
            Vec3f c = cam.viewPosToRay(r);
            Vec3f b = moveAxis.clone();
            float d;
            d = (a.y * c.x - a.x * c.y) / (b.x * c.y - b.y * c.x) * distToObj;
            if (!Float.isFinite(d)) {
                d = (a.y * c.z - a.z * c.y) / (b.z * c.y - b.y * c.z) * distToObj;
                if (!Float.isFinite(d)) {
                    d = (a.x * c.z - a.z * c.x) / (b.z * c.x - b.x * c.z) * distToObj;
                }
            }
            cube.loc = objPos.add_(b.scale(d));
             */
            repaint();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_G:
                if (grab) {
                    grab = false;
                } else {
                    grab = true;
                    lastGrabX = mouseX;
                    lastGrabY = mouseY;
                    objPos = cube.loc.clone();
                    objRot = cube.rot.clone();
                    distToObj = cube.loc.sub_(cam.loc).norm();
                    moveAxis = Vec3f.rand_().normalize();
                }
                repaint();
                break;
            case KeyEvent.VK_X:
                if(grab) {
                    moveAxis = new Vec3f(1.0f, 0.0f, 0.0f);
                    repaint();
                }
                break;
            case KeyEvent.VK_Y:
                if(grab) {
                    moveAxis = new Vec3f(0.0f, 1.0f, 0.0f);
                    repaint();
                }
                break;
            case KeyEvent.VK_Z:
                if(grab) {
                    moveAxis = new Vec3f(0.0f, 0.0f, 1.0f);
                    repaint();
                }
                break;
            case KeyEvent.VK_SPACE:
                /*
                for (int i = 0; i < getWidth(); i+=20) {
                    for (int j = 0; j < getHeight(); j+=20) {
                        Vec3f a = cam.viewPosToRay(i, j, getWidth(), getHeight());
                        Vec3f p = a.scale(cam.distToTarget).add(cam.loc);

                        points.add(p);
                    }
                }
                 */

                Vec3f a = cam.viewPosToRay(mouseX, mouseY, getWidth(), getHeight());
                Vec3f p = a.scale(cam.distToTarget).add(cam.loc);

                lines.add(p);
                lines.add(cam.loc);

                points.add(p);

                repaint();
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
