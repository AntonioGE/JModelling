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
package jmodelling.engine.editor.viewport.object.tools;

import com.jogamp.opengl.GLAutoDrawable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.object.ObjectMode;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.raytracing.Raytracer;
import jmodelling.engine.scene.Scene;
import jmodelling.math.vec.Vec2f;

/**
 *
 * @author ANTONIO
 */
public class Select extends ObjectTool {

    protected int lastPressX, lastPressY;

    public Select(View3D editor, ObjectMode objectMode) {
        super(editor, objectMode);
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

        if (editor.isShiftPressed()) {
            if (SwingUtilities.isRightMouseButton(e)) {
                List<MeshObject> objsSelected = Raytracer.getIntersectingMeshObjects(
                        editor.getCam().loc,
                        editor.getCam().viewPosToRay(e.getX(), e.getY(), editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                        editor.getScene().getMeshObjects());

                if (objsSelected.size() == 1) {
                    //editor.getScene().selectObject(object)
                }

                for (MeshObject obj : objsSelected) {
                    System.out.println(obj.name);
                }
                System.out.println();
                editor.repaintSameEditors();
            }
        } else {
            if (SwingUtilities.isRightMouseButton(e)) {
                final List<MeshObject> objsSelected = Raytracer.getIntersectingMeshObjects(
                        editor.getCam().loc,
                        editor.getCam().viewPosToRay(e.getX(), e.getY(), editor.getPanel().getWidth(), editor.getPanel().getHeight()),
                        editor.getScene().getMeshObjects());
                
                final Scene scene = editor.getScene();
                if (objsSelected.size() == 1) {
                    scene.selectOnlyObject(objsSelected.get(0));
                }else if(objsSelected.size() > 1){
                    if(scene.getLastSelectedObject() == objsSelected.get(0)){
                        scene.selectOnlyObject(objsSelected.get(1));
                    }else{
                        if(new Vec2f(lastPressX, lastPressY).dist(new Vec2f(e.getX(), e.getY())) < 2.0f){
                            int index = objsSelected.indexOf(scene.getLastSelectedObject());
                            scene.selectOnlyObject(objsSelected.get((index + 1) % objsSelected.size()));
                        }else{
                            scene.selectOnlyObject(objsSelected.get(0));
                        }
                    }
                }
                editor.repaintSameEditors();
            }
        }
        lastPressX = e.getX();
        lastPressY = e.getY();
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
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:{
                if(editor.getScene().areAllObjectsSelected()){
                    editor.getScene().deselectAll();
                }else{
                    editor.getScene().selectAll();
                }
                editor.repaintSameEditors();
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
