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
package jmodelling.engine.editor.viewport.edit.tools;

import com.jogamp.opengl.GLAutoDrawable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.edit.EditMode;
import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.object.mesh.emesh.Edge;
import jmodelling.engine.raytracing.MeshRaytracer;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Select extends EditTool {

    public Select(View3D editor, EditMode editMode) {
        super(editor, editMode);
    }

    @Override
    public void init(GLAutoDrawable glad) {

    }

    @Override
    public void dispose(GLAutoDrawable glad) {
    }

    @Override
    public void display(GLAutoDrawable glad) {
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            Edge edge = MeshRaytracer.getEdgeSolid(
                    editor.getCam().viewPosToRay(e.getX(), e.getY(), editor.panel.getWidth(), editor.panel.getHeight()),
                    Cam.pixelToView(e.getX(), e.getY(), editor.panel.getWidth(), editor.panel.getHeight()),
                    mode.obj,
                    editor.getTransf(),
                    0.1f, 0.005f);
            if (edge != null) {
                mode.obj.emesh.selectVtx(edge.v0);
                mode.obj.emesh.selectVtx(edge.v1);
                editor.getScene().update(mode.obj);
                editor.repaintSameEditors();
            }
        }
        /*
        if (SwingUtilities.isRightMouseButton(e)) {
            Vec3f vtx = MeshRaytracer.getVtxSolid(
                    editor.getCam().viewPosToRay(e.getX(), e.getY(), editor.panel.getWidth(), editor.panel.getHeight()),
                    Cam.pixelToView(e.getX(), e.getY(), editor.panel.getWidth(), editor.panel.getHeight()),
                    mode.obj,
                    editor.getTransf(),
                    0.1f, 0.005f);
            if (vtx != null) {
                if (editor.isShiftPressed()) {
                    if (mode.obj.emesh.isVtxSelected(vtx)) {
                        mode.obj.emesh.deselectVtx(vtx);
                        editor.getScene().update(mode.obj);
                        editor.repaintSameEditors();
                    } else {
                        mode.obj.emesh.selectVtx(vtx);
                        editor.getScene().update(mode.obj);
                        editor.repaintSameEditors();
                    }
                } else {
                    mode.obj.emesh.deselectAllVtxs();
                    mode.obj.emesh.selectVtx(vtx);
                    editor.getScene().update(mode.obj);
                    editor.repaintSameEditors();

                }
            }
        }*/
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
        editor.moveCamera(e);
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
            case KeyEvent.VK_SPACE: {
                List<Vec3f> list = new ArrayList<>(mode.obj.emesh.vtxs.size());
                for (Vec3f vtx : mode.obj.emesh.vtxs) {
                    list.add(vtx.clone());
                }
                mode.obj.emesh.selectVtxs(list);
                break;
            }
            case KeyEvent.VK_Q: {

                break;
            }
        }
        mode.changeTool(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        editor.zoomCamera(e);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void exitTool() {
    }

}
