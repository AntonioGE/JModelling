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
package jmodelling.engine.object.newmesh;

import com.jogamp.opengl.GL2;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmodelling.engine.formats.obj.ObjReader;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.bounds.BoundingBox;
import jmodelling.engine.object.bounds.BoundingSphere;
import jmodelling.engine.object.cmesh2.CMesh2;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class MeshObject3 extends Object3D {

    public CMesh2 cmesh;
    public MeshGL3 meshGL;

    private BoundingSphere boundingSphere;

    public MeshObject3(String name, Vec3f loc, CMesh2 cmesh) {
        super(name, loc);

        this.cmesh = cmesh;
        this.meshGL = new MeshGL3(cmesh);
    }

    //TODO: Temporary function, remove it later
    public MeshObject3(String objPath) {
        HashMap<String, Mesh> meshes;
        try {
            meshes = ObjReader.readObj(objPath);
            //this.cmesh = new CMesh2(meshes.get("Suzanne"));
            this.cmesh = new CMesh2(meshes.get("spot"));
            //this.cmesh = new CMesh2(meshes.get("Plane"));
            meshGL = new MeshGL3(cmesh);

            calculateBounds();
        } catch (IOException ex) {
            Logger.getLogger(MeshObject3.class.getName()).log(Level.SEVERE, null, ex);
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
        return "MESH3";
    }

    public final void calculateBounds() {
        //BoundingBox boundingBox = new BoundingBox(cmesh);
        //this.boundingSphere = new BoundingSphere(boundingBox);
    }

}
