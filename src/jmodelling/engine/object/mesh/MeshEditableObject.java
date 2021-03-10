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
package jmodelling.engine.object.mesh;

import com.jogamp.opengl.GL2;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.bounds.BoundingSphere;
import jmodelling.engine.object.mesh.cmesh.CMesh;
import jmodelling.engine.object.mesh.emesh.EMesh;
import jmodelling.engine.object.mesh.emesh.gl.EMeshGL;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class MeshEditableObject extends Object3D {

    public EMesh emesh;
    public EMeshGL emeshGL;

    public MeshEditableObject(String name, Vec3f loc, CMesh cmesh) {
        super(name, loc);
        
        this.emesh = new EMesh(cmesh);
        this.emeshGL = new EMeshGL(emesh);
    }

    public MeshEditableObject(MeshObject meshObject){
        this(meshObject.name + " EDIT", meshObject.loc.clone(), meshObject.cmesh);
    }
    
    //TODO: move to renderer?
    @Override
    public void renderOpaque(GL2 gl) {
        if (emesh.resized) {

            emesh.resized = false;
            emesh.edited = false;
        } else if (emesh.edited) {

            emesh.resized = false;
            emesh.edited = false;
        }
        gl.glPushMatrix();

        gl.glMultMatrixf(getLocalAxis().toArray(), 0);
        emeshGL.render(gl);

        gl.glPopMatrix();
    }

    @Override
    public void renderWireframe(GL2 gl) {

    }

    @Override
    public void init(GL2 gl) {
        emeshGL.init(gl);
    }

    @Override
    public void update(GL2 gl) {
        emeshGL.update(gl);
    }

    @Override
    public void delete(GL2 gl) {
        emeshGL.update(gl);
    }

    @Override
    public BoundingSphere getBoundingSphere() {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public String getType() {
        return "MESH_EDITABLE_OBJECT";
    }

}
