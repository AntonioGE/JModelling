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
package jmodelling.engine.editor.viewport.edit;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import jmodelling.engine.Engine;
import jmodelling.engine.editor.Tool;
import jmodelling.engine.editor.viewport.Mode;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.edit.tools.EditTool;
import jmodelling.engine.editor.viewport.edit.tools.Grab;
import jmodelling.engine.editor.viewport.edit.tools.Select;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.mesh.MeshEditableObject;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.object.mesh.emesh.gl.EShapeGL;
import jmodelling.engine.raytracing.MeshRaytracer;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class EditMode extends Mode {

    public static final String NAME = "EDIT MODE";
    public Tool tool;
    public final MeshEditableObject obj;

    List<Vec3f> points = new ArrayList<>();
    
    
    public EditMode(View3D editor, Engine engine, MeshEditableObject obj) {
        super(editor, engine);

        this.tool = new Select(editor, this);
        this.obj = obj;

    }

    @Override
    public void init(GLAutoDrawable glad) {
        if (tool != null) {
            tool.init(glad);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        if (tool != null) {
            tool.dispose(glad);
        }
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        if (obj.emesh.edited || obj.emesh.resized) {
            engine.scene.update(obj);
        }

        engine.scene.updateGL(gl);

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        /**
         * Render object to edit
         */
        obj.renderOpaque(gl);

        /**
         * Render unselected objects
         */
        engine.scene.getUnselectedObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        /**
         * Render HUD
         */
        gl.glDisable(GL2.GL_LIGHTING);
        engine.scene.getHudObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        /**
         * Render selected objects outline
         */
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDepthMask(false);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glLineWidth(2);

        final Object3D lastSelected = engine.scene.getLastSelectedObject();
        gl.glColor3f(0.945f, 0.345f, 0.0f);
        for (Object3D obj : engine.scene.getSelectedObjects()) {
            if (obj != lastSelected) {
                obj.renderWireframe(gl);
            }
        }

        /**
         * Render selected objects
         */
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDepthMask(true);
        gl.glLineWidth(1);
        engine.scene.getSelectedObjects().forEach((obj) -> {
            if (obj != lastSelected) {
                obj.renderOpaque(gl);
            }
        });

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL2.GL_LIGHTING);
        
        gl.glBegin(GL2.GL_POINTS);
        for(Vec3f p : points){
            gl.glVertex3f(p.x, p.y, p.z);
        }
        gl.glEnd();

    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        if (tool != null) {
            tool.reshape(glad, x, y, width, height);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (tool != null) {
            tool.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (tool != null) {
            tool.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (tool != null) {
            tool.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (tool != null) {
            tool.mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (tool != null) {
            tool.mouseExited(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (tool != null) {
            tool.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (tool != null) {
            tool.mouseMoved(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (tool != null) {
            tool.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                setDefaultTool();
                editor.panel.repaint();
                break;
            }
            case KeyEvent.VK_TAB: {
                System.out.println("TAB EDIT MODE");
                changeMode();
                engine.repaintDisplaysUsingEditor(editor);
                break;
            }
            case KeyEvent.VK_SPACE: {
                List<Vec3f> inters = MeshRaytracer.rayEMeshIntersections(editor.getCam().viewPosToRay(editor.getMouseX(), editor.getMouseY(), editor.getPanel().getWidth(), editor.getPanel().getHeight()), obj);
                if(!inters.isEmpty()){
                    points.add(inters.get(0));
                }
                engine.repaintDisplaysUsingEditor(editor);
                break;
            }
            default: {
                if (tool != null) {
                    tool.keyPressed(e);
                } else {
                    changeTool(e);
                }
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (tool != null) {
            tool.keyReleased(e);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (tool != null) {
            tool.mouseWheelMoved(e);
        }
    }

    @Override
    public void destroy() {
        if (this.tool != null) {
            tool.destroy();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void setTool(EditTool tool) {
        if (this.tool != null) {
            tool.destroy();
        }
        this.tool = tool;
    }

    public void setDefaultTool() {
        setTool(new Select(editor, this));
    }

    public void changeTool(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                setDefaultTool();
                break;
            }

            case KeyEvent.VK_G: {
                if (!obj.emesh.selectedVtxs.isEmpty()) {
                    setTool(new Grab(editor, this));
                }
                break;
            }
        }
    }

    public void changeMode() {
        //editor.getScene().setLastSelectedObject(new MeshObject(obj));
        editor.getScene().finishEditSelectedObject();
        editor.setModesInEditors(ObjectMode.NAME);
    }

}
