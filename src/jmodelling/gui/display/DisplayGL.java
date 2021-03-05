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
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.object.camera.CamArcball;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.object.mesh.utils.triangulator.EarClipping;
import jmodelling.engine.object.hud.AxisSmall;
import jmodelling.engine.raytracing.Raytracer;
import jmodelling.engine.scene.Scene;
import jmodelling.engine.transform.Transformation;
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

    private AxisSmall axis = new AxisSmall("",
            new Vec3f(0.5f, 0.0f, 0.5f),
            new Vec3f(21.0f, 33.0f, 33.0f),
            new Vec3f(1.0f, 1.0f, 1.0f)
    );

    private final int w = 10, h = 100;
    private AxisSmall[] cosas = new AxisSmall[w * h];

    private MeshObject cube;

    private boolean grab = false;
    private int lastGrabX, lastGrabY;
    private int mouseX, mouseY;
    private Vec3f objPos = new Vec3f();
    private Vec3f objRot = new Vec3f();
    private Vec3f objSca = new Vec3f();
    private float distToObj;
    private Mat4f transf;
    private Vec3f moveAxis;

    private ArrayList<Vec3f> points = new ArrayList<Vec3f>() {
        {

        }
    };

    private ArrayList<Vec3f> lines = new ArrayList<Vec3f>();

    private Scene scene = new Scene();

    //private NewMeshObject nObject = new NewMeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\mono.obj");
    //private MeshObject nObject = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Spot.obj");
    private MeshObject nObject = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\mono.obj");
    //private MeshObject nObject = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\cylinder.obj");
    //private NewMeshObject nObject = new NewMeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\cubo.obj");
    //private NewMeshObject nObject = new NewMeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\plane.obj");

    private TextRenderer textRenderer;
    private MeshObject objectSelected = null;

    private List<Vec3f> vtxs = new ArrayList<>();
    private List<Vec3f> tris = new ArrayList<>();
    private float firstDist = 0.0f;

    private MeshObject mesh3;

    public DisplayGL() {
        super(generateCapabilities());

        for (int i = 0, c = 0; i < w; i++) {
            for (int j = 0; j < h; j++, c++) {
                cosas[c] = new AxisSmall("",
                        new Vec3f(i, j, (float) Math.random()),
                        new Vec3f((float) Math.random() * 360.0f, (float) Math.random() * 360.0f, (float) Math.random() * 360.0f),
                        new Vec3f(1.0f, 1.0f, 1.0f));
            }
        }

        //scene.add(nObject);
        //nObject.loc.set(5.0f, 2.0f, 1.0f);
        MeshObject temp = new MeshObject("Temp", new Vec3f(), nObject.cmesh);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                MeshObject newObject = new MeshObject("Nuevo " + i + " " + j, new Vec3f(), temp.cmesh);
                System.out.println(newObject.name);
                newObject.loc.x = i * 6.0f;
                newObject.loc.y = j * 6.0f;
                newObject.rot.x = (float) (Math.random() * 360.0f);
                newObject.rot.y = (float) (Math.random() * 360.0f);
                newObject.rot.z = (float) (Math.random() * 360.0f);
                //newObject.sca.x = (float) (Math.random() * 2.0f);
                //newObject.sca.y = (float) (Math.random() * 2.0f);
                //newObject.sca.z = (float) (Math.random() * 2.0f);
                newObject.sca.x = 0.2f;
                newObject.sca.y = 0.8f;
                newObject.sca.z = 0.5f;
                scene.addObject(newObject);
            }
        }
        //mesh3 = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Spot.obj");
        mesh3 = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\mono.obj");
        //mesh3 = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\cylinder.obj");
        //mesh3 = new MeshObject3("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\monoOriginal.obj");
        //mesh3 = new MeshObject3("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\poly.obj");
        mesh3.loc.set(0.0f, 0.0f, 4.0f);
        mesh3.rot.set(45.0f, -20.0f, 14.0f);
        mesh3.sca.set(1f, 1f, 0.2f);
        scene.addObject(mesh3);

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

        gl.glEnable(GL2.GL_STENCIL_TEST);
        gl.glStencilFunc(GL2.GL_NOTEQUAL, 1, 0xFF);
        gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);

        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12), true, false);

        //nObject.meshGL.init(gl);
        scene.updateGL(gl);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.out.println("Dispose");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);

        /*
        gl.glDisable(GL2.GL_DEPTH_TEST);
        gl.glLoadIdentity();
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex2f(-1.0f, -1.0f);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex2f(1.0f, -1.0f);
        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glVertex2f(1.0f, 1.0f);
        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glVertex2f(-1.0f, 1.0f);
        gl.glEnd();*/
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_BLEND);
        //gl.glEnable(GL2.GL_NORMALIZE);
        //gl.glEnable(GL2.GL_RESCALE_NORMAL);
        ///////////////////
        gl.glLoadIdentity();

        /*
        gl.glDepthFunc(GL2.GL_LESS);
        gl.glLineWidth(3);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glBegin(GL2.GL_LINES);
        for(int i = 0; i < vtxs.size(); i++){
            Vec3f v1 = vtxs.get(i);
            Vec3f v2 = vtxs.get((i + 1) % vtxs.size());
            gl.glVertex3f(v1.x, v1.y, v1.z);
            gl.glVertex3f(v2.x, v2.y, v2.z);
        }
        gl.glEnd();
        
        gl.glDepthFunc(GL2.GL_LESS);
        gl.glLineWidth(1);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_TRIANGLES);
        tris.forEach((v)->{
            gl.glVertex3f(v.x, v.y, v.z);
        });
        gl.glEnd();
        
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glLineWidth(1);
        gl.glDepthFunc(GL2.GL_ALWAYS);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glBegin(GL2.GL_TRIANGLES);
        tris.forEach((v)->{
            gl.glVertex3f(v.x, v.y, v.z);
        });
        gl.glEnd();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
         */
        ////////////////////
        gl.glLoadIdentity();
        lighting(gl);
        //lighting2(gl);

        Mat4f p = TransfMat.perspective_(cam.fov, (float) getWidth() / getHeight(), 0.1f, 1000.0f);
        Mat4f rx = TransfMat.rotationDeg_(-cam.rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotationDeg_(-cam.rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotationDeg_(-cam.rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f t = TransfMat.translation_(cam.loc.negate_());

        transf = p.mul_(rx).mul(ry).mul(rz).mul(t);

        //transf.print();
        //cam.getLocalAxis3f().print();
        gl.glLoadIdentity();
        gl.glMultMatrixf(p.toArray(), 0);
        gl.glMultMatrixf(rx.toArray(), 0);
        gl.glMultMatrixf(ry.toArray(), 0);
        gl.glMultMatrixf(rz.toArray(), 0);
        gl.glMultMatrixf(t.toArray(), 0);

        gl.glLoadMatrixf(transf.toArray(), 0);

        gl.glColor4f(0.0f, 1.0f, 1.0f, 1.0f);

        //Stencil
        /*
        gl.glClearStencil(0);
        gl.glClear(GL2.GL_STENCIL_BUFFER_BIT);
        gl.glEnable(GL2.GL_STENCIL_TEST);
        gl.glStencilFunc(GL2.GL_ALWAYS, 1, -1);
        gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);
         */
        gl.glDisable(GL2.GL_STENCIL_TEST);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDepthFunc(GL2.GL_ALWAYS);
        gl.glPointSize(4.0f);
        gl.glBegin(GL2.GL_POINTS);
        for (Vec3f point : points) {
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glVertex3f(point.x, point.y, point.z);
        }
        gl.glEnd();
        gl.glDepthFunc(GL2.GL_LESS);

        gl.glEnable(GL2.GL_LIGHTING);
        scene.updateGL(gl);
        scene.getObjects().forEach((obj) -> {
            if (obj != objectSelected) {
                obj.renderOpaque(gl);
            }
        });

        /*
        scene.updateGL(gl);
        gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
        gl.glPolygonOffset(1.0f, 1.0f);
        scene.getObjects().forEach((obj) -> {
            if (obj != objectSelected) {
                obj.renderOpaque(gl);
            }
        });
        gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
        
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        scene.getObjects().forEach((obj) -> {
            if (obj != objectSelected) {
                obj.renderOpaque(gl);
            }
        });
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
         */
        if (objectSelected != null) {
            //Stencil
            gl.glClearStencil(0);
            gl.glClear(GL2.GL_STENCIL_BUFFER_BIT);
            gl.glEnable(GL2.GL_STENCIL_TEST);
            gl.glStencilFunc(GL2.GL_ALWAYS, 1, -1);
            gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);

            objectSelected.renderOpaque(gl);

            gl.glStencilFunc(GL2.GL_NOTEQUAL, 1, -1);
            gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);
            gl.glLineWidth(2);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

            gl.glDisable(GL2.GL_LIGHTING);

            objectSelected.renderOpaque(gl);

            gl.glLineWidth(1);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        }

        gl.glDisable(GL2.GL_LIGHTING);

        gl.glDepthFunc(GL2.GL_LESS);

        //cube.renderOpaque(gl);
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

        for (AxisSmall axis : cosas) {
            axis.renderOpaque(gl);
        }

        if (grab) {
            gl.glPushMatrix();
            gl.glTranslatef(objPos.x, objPos.y, objPos.z);
            //objPos.print();
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

        cam.getUp().print();
        
        //cam.getDir().print();
        //new Vec3f(0.0f, 0.0f, -1.0f).mul(cam.getLocalAxis3f()).print();
        textRenderer.beginRendering(getWidth(), getHeight());
        // optionally set the color
        textRenderer.setColor(0.0f, 0.0f, 0.0f, 0.9f);
        textRenderer.draw(System.getProperty("java.version"), 5, 5);
        // ... more draw commands, color changes, etc.
        textRenderer.endRendering();

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
        requestFocus();
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

 /*
            Mat3f rot = Transform.planarRotation_(objPos, 
                    Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight()),
                    Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight()), 
                    transf, cam, (float) getWidth() / getHeight());
            
            Vec3f angles = TransfMat.matToEulerDeg_(rot.mul(TransfMat.eulerDegToMat_(objRot)));
            cube.rot.set(angles);*/
 /*
            Mat3f rot = Transform.axisRotation_(objPos, 
                    new Vec3f(1.0f, 0.0f, 0.0f),
                    Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight()),
                    Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight()), 
                    transf, cam, (float) getWidth() / getHeight());
            
            Vec3f angles = TransfMat.matToEulerDeg_(rot.mul(TransfMat.eulerDegToMat_(objRot)));
            cube.rot.set(angles);
             */
            Vec3f sca = Transformation.scale_(objPos,
                    Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight()),
                    Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight()),
                    transf, (float) getWidth() / getHeight());

            cube.sca.set(objSca.had_(sca));

            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                scene.addObject(nObject);
                repaint();
                break;
            case KeyEvent.VK_D:
                scene.removeObject(nObject);
                repaint();
                break;
            case KeyEvent.VK_G:
                if (grab) {
                    grab = false;
                } else {
                    grab = true;
                    lastGrabX = mouseX;
                    lastGrabY = mouseY;
                    objPos = cube.loc.clone();
                    objRot = cube.rot.clone();
                    objSca = cube.sca.clone();
                    distToObj = cube.loc.sub_(cam.loc).norm();
                    moveAxis = Vec3f.rand_().normalize();
                }
                repaint();
                break;
            case KeyEvent.VK_R:
                MeshObject objSelected = Raytracer.getSelectedMeshObject(cam.loc,
                        cam.viewPosToRay(mouseX, mouseY, getWidth(), getHeight()),
                        scene.getMeshObjects());
                if (objSelected != null) {
                    System.out.println(objSelected.name);
                    this.objectSelected = objSelected;
                }

                Vec3f point = Raytracer.getClosestIntersectionPoint(cam.loc,
                        cam.viewPosToRay(mouseX, mouseY, getWidth(), getHeight()),
                        scene.getMeshObjects());
                if (point != null) {
                    points.add(point);
                    repaint();
                }
                /*
                System.out.println("Num objects: " + scene.getObjects().size());
                for (Object3D obj : scene.getObjects()) {
                    if (obj.isSelectable()) {
                        if (Raytracer.rayIntersectsBoundingSphere(cam.loc,
                                cam.viewPosToRay(mouseX, mouseY, getWidth(), getHeight()),
                                obj)) {
                            System.out.println("INTERSECT SPHERE " + obj.name + " " + System.currentTimeMillis());
                        }
                    }
                }*/
                break;
            case KeyEvent.VK_T:
                long before0 = System.nanoTime();
                int delta = 10;
                for (int i = 0; i < getWidth(); i += delta) {
                    for (int j = 0; j < getHeight(); j += delta) {
                        Vec3f p = Raytracer.getClosestIntersectionPoint(cam.loc,
                                cam.viewPosToRay(i, j, getWidth(), getHeight()),
                                scene.getMeshObjects());
                        if (p != null) {
                            //points.add(p);
                        }
                    }
                }
                System.out.println((System.nanoTime() - before0) + " ns elapsed");

                long before1 = System.nanoTime();
                Collection<Vec3f> newPoints = Collections.synchronizedList(new ArrayList<Vec3f>());
                Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
                for (int t = 0; t < threads.length; t++) {
                    Thread thread = new Thread() {
                        Collection<Vec3f> newPoints;
                        int id;

                        public Thread init(int id, Collection<Vec3f> newPoints) {
                            this.id = id;
                            this.newPoints = newPoints;
                            return this;
                        }

                        @Override
                        public void run() {
                            final int delta = 10;
                            final int size = getWidth() / threads.length;
                            for (int i = 0; i < getWidth() / threads.length; i += delta) {
                                for (int j = 0; j < getHeight(); j += delta) {
                                    Vec3f p = Raytracer.getClosestIntersectionPoint(cam.loc,
                                            cam.viewPosToRay(i + id * size, j, getWidth(), getHeight()),
                                            scene.getMeshObjects());
                                    if (p != null) {
                                        newPoints.add(p);
                                    }
                                }
                            }
                        }
                    }.init(t, newPoints);
                    threads[t] = thread;
                    thread.start();
                }
                for (Thread thread : threads) {
                    try {
                        thread.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DisplayGL.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                points.addAll(newPoints);
                System.out.println((System.nanoTime() - before1) + " ns elapsed");

                repaint();
                break;
            case KeyEvent.VK_Q: {
                //Vec3f a = cam.viewPosToRay(mouseX, mouseY, getWidth(), getHeight());
                //Vec3f p = a.scale(cam.distToTarget).add(cam.loc);
                Vec3f p = new Vec3f(
                        ((float) mouseX / getWidth()) * 2.0f - 1.0f,
                        -(((float) mouseY / getHeight()) * 2.0f - 1.0f),
                        0.0f);

                vtxs.add(p);

                if (vtxs.size() > 3) {
                    List<Integer> inds = EarClipping.triangulate(vtxs);
                    tris = new ArrayList<>();
                    inds.forEach((i) -> {
                        tris.add(vtxs.get(i));
                    });
                }
                repaint();
            }
            case KeyEvent.VK_X:
                if (grab) {
                    moveAxis = new Vec3f(1.0f, 0.0f, 0.0f);
                    repaint();
                }
                break;
            case KeyEvent.VK_Y:
                if (grab) {
                    moveAxis = new Vec3f(0.0f, 1.0f, 0.0f);
                    repaint();
                }
                break;
            case KeyEvent.VK_Z:
                if (grab) {
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

    //TODO: This should be put into a custom OpenGL panel initialization code
    private static GLCapabilities generateCapabilities() {
        final GLProfile gp = GLProfile.get(GLProfile.GL2);
        GLCapabilities cap = new GLCapabilities(gp);
        //cap.setSampleBuffers(true);
        //cap.setNumSamples(8);
        cap.setStencilBits(8);
        return cap;
    }

    private void lighting(GL2 gl) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);

        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{1.0f, 1.0f, 0.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.3f, 0.3f, 0.3f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{0.1f, 0.1f, 0.1f, 0.0f}, 0);

        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[]{0.5f, 0.5f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[]{-1.0f, -1.0f, 0.0f, 0.0f}, 0);

    }

    private void lighting2(GL2 gl) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);

        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{1.0f, 1.0f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.3f, 0.3f, 0.3f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{0.1f, 0.1f, 0.1f, 0.0f}, 0);

        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[]{0.5f, 0.5f, 1.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[]{-1.0f, -1.0f, 0.0f, 0.0f}, 0);

    }

}
