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
import java.io.IOException;
import java.util.HashMap;
import jmodelling.engine.formats.obj.ObjReader;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.bounds.BoundingBox;
import jmodelling.engine.object.bounds.BoundingSphere;
import jmodelling.engine.object.mesh.cmesh.CMesh;
import jmodelling.engine.object.mesh.cmesh.gl.CMeshGL;
import jmodelling.engine.object.mesh.emesh.EMesh;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class MeshObject extends Object3D {

    public CMesh cmesh;
    public CMeshGL meshGL;

    private BoundingSphere boundingSphere;

    public MeshObject(String name, Vec3f loc, CMesh cmesh) {
        super(name, loc);

        this.cmesh = cmesh;
        this.meshGL = new CMeshGL(cmesh);
        
        calculateBounds();
    }

    //TODO: Temporary function, remove it later
    public MeshObject(String objPath) {
        HashMap<String, EMesh> meshes;
        try {
            meshes = ObjReader.readObj(objPath);
            //this.cmesh = new CMesh(meshes.get("Cylinder"));
            this.cmesh = new CMesh(meshes.get("Suzanne"));
            //this.cmesh = new CMesh(meshes.get("spot"));
            //this.cmesh = new CMesh(meshes.get("Plane"));
            meshGL = new CMeshGL(cmesh);

            calculateBounds();
        } catch (IOException ex) {

        }

    }

    @Override
    public void renderOpaque(GL2 gl) {
        gl.glPushMatrix();
        
        gl.glMultMatrixf(getLocalAxis().toArray(), 0);
        meshGL.render(gl);

        gl.glPopMatrix();
    }

    @Override
    public void renderWireframe(GL2 gl) {
        gl.glPushMatrix();
        
        gl.glMultMatrixf(getLocalAxis().toArray(), 0);
        meshGL.renderWireframe(gl);

        gl.glPopMatrix();
    }

    @Override
    public void init(GL2 gl) {
        meshGL.init(gl);
    }

    @Override
    public void update(GL2 gl) {
        meshGL.update(gl, meshGL);
    }

    @Override
    public void delete(GL2 gl) {
        meshGL.delete(gl);
    }

    @Override
    public BoundingSphere getBoundingSphere() {
        return boundingSphere;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public String getType() {
        return "MESH";
    }

    public final void calculateBounds() {
        BoundingBox boundingBox = new BoundingBox(cmesh);
        this.boundingSphere = new BoundingSphere(boundingBox);
    }
}
