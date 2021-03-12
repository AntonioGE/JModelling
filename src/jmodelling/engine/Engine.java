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
package jmodelling.engine;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;
import java.util.List;
import jmodelling.engine.editor.Editor;
import jmodelling.engine.editor.viewport.Mode;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.object.hud.Axis;
import jmodelling.engine.object.hud.Grid;
import jmodelling.engine.object.mesh.MeshEditableObject;
import jmodelling.engine.object.mesh.cmesh.CMesh;
import jmodelling.engine.object.mesh.emesh.EMesh;
import jmodelling.engine.object.mesh.emesh.gl.EMeshGL;
import jmodelling.engine.scene.Scene;
import jmodelling.gui.MainFrame;
import jmodelling.gui.display.EditorDisplayGL;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Engine {

    private final MainFrame frame;
    public Scene scene;

    public final GLAutoDrawable sharedDrawable;
    
    public final TextRenderer textRenderer;
    
    public Engine(MainFrame frame) {
        this.frame = frame;

        //final GLCapabilitiesImmutable caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        final GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        //caps.setStencilBits(8);
        //caps.setSampleBuffers(true);
        //caps.setNumSamples(2);
        caps.setDepthBits(24);
        //caps.setDoubleBuffered(true);
        final GLProfile glp = caps.getGLProfile();

        final boolean createNewDevice = true;
        sharedDrawable = GLDrawableFactory.getFactory(glp).createDummyAutoDrawable(null, createNewDevice, caps, null);
        sharedDrawable.display();

        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12), true, false);
        
        scene = new Scene();
        //scene.addHudObject(new Axis());
        scene.addHudObject(new Grid(16, 1.0f));
        //scene.add(new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\cylinder.obj"));
        // obj = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\mono.obj");
        MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\monoOriginal.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\ManyMonos.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\plane.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Sphere.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Shape.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Spot.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Arceus.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\cylinder.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Cono.obj");

        System.out.print("Starting CMesh->EMesh... ");
        long before = System.nanoTime();
        EMesh emesh = new EMesh(mono.cmesh);
        System.out.println("FINISHED " + (System.nanoTime() - before));

        System.out.print("Starting Flat Shading... ");
        before = System.nanoTime();
        //emesh.applyFlatShading();
        System.out.println("FINISHED " + (System.nanoTime() - before));

        System.out.print("Starting EMesh->CMesh... ");
        before = System.nanoTime();
        mono.cmesh = new CMesh(emesh);
        System.out.println("FINISHED " + (System.nanoTime() - before));

        System.out.print("Starting EMesh->EMeshGL... ");
        before = System.nanoTime();
        EMeshGL emeshGL = new EMeshGL(emesh);
        System.out.println("FINISHED " + (System.nanoTime() - before));
        
        /*
        MeshEditableObject meshEditable = new MeshEditableObject("Ediatable", 
                new Vec3f(-6.0f, 0.0f, 0.0f), mono.cmesh);
        scene.addObject(meshEditable);
        */
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                MeshObject obj = new MeshObject("Monito " + i + " " + j,
                        new Vec3f(i * 4.0f, j * 4.0f, 0.0f), mono.cmesh.clone());
                scene.addObject(obj);
                //obj.sca.set(0.01f, 0.01f, 0.01f);
            }
        }
        scene.selectAll();
    }

    public void repaintDisplaysUsingEditor(Editor editor) {
        for (EditorDisplayGL display : frame.editorDisplays) {
            if (display.getEditor().getName().equals(editor.getName())) {
                display.repaint();
            }
        }
    }
    
    public List<EditorDisplayGL> getEditorDisplays(){
        return frame.editorDisplays;
    }

}
