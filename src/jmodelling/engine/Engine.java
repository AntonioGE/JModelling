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
import jmodelling.engine.editor.Editor;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.object.hud.Axis;
import jmodelling.engine.object.mesh.cmesh.CMesh;
import jmodelling.engine.object.mesh.emesh.EMesh;
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

    public Engine(MainFrame frame) {
        this.frame = frame;

        //final GLCapabilitiesImmutable caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        final GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        //caps.setStencilBits(8);
        //caps.setSampleBuffers(true);
        //caps.setNumSamples(2);
        final GLProfile glp = caps.getGLProfile();

        final boolean createNewDevice = true;
        sharedDrawable = GLDrawableFactory.getFactory(glp).createDummyAutoDrawable(null, createNewDevice, caps, null);
        sharedDrawable.display();

        scene = new Scene();
        scene.addHudObject(new Axis());
        //scene.add(new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\cylinder.obj"));
        // obj = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\mono.obj");
        MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\monoOriginal.obj");
        //MeshObject mono = new MeshObject("C:\\Users\\ANTONIO\\Documents\\cosa a borrar\\Beach_HGSS\\Cono.obj");
        mono.cmesh = new CMesh(new EMesh(mono.cmesh));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                MeshObject obj = new MeshObject("Monito" + i + " " + j,
                        new Vec3f(i * 4.0f, j * 4.0f, 0.0f), mono.cmesh.clone());
                scene.addObject(obj);
            }
        }
        scene.selectAll();
    }

    public void updateDisplaysUsingEditor(Editor editor) {
        for (EditorDisplayGL display : frame.editorDisplays) {
            if (display.getEditor().getEditorName().equals(editor.getEditorName())) {
                display.repaint();
            }
        }
    }

}
