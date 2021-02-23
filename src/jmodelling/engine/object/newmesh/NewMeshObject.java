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

/**
 *
 * @author ANTONIO
 */
public class NewMeshObject extends Object3D{

    public Mesh mesh;
    public MeshGL meshGL;
    
    private BoundingSphere boundingSphere;
    
    public NewMeshObject(String name, Mesh mesh){
        this.mesh = mesh;
        this.meshGL = new MeshGL(mesh);
        
        calculateBounds();
    }
    
    //TODO: Temp function
    public NewMeshObject(String objPath){
        try {
            HashMap<String, Mesh> meshes = ObjReader.readObj(objPath);
            this.mesh = meshes.get("Suzanne");
            //this.mesh = meshes.get("Suzanne_Cube");
            //this.mesh = meshes.get("Plane");
            
            //mesh.applyFlatShading();
            
            meshGL = new MeshGL(mesh);
            calculateBounds();
            
        } catch (IOException ex) {
            Logger.getLogger(NewMeshObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public NewMeshObject(NewMeshObject other){
        this.mesh = other.mesh.clone();
        meshGL = new MeshGL(mesh);
        
        calculateBounds();
    }
    
    @Override
    public NewMeshObject clone() {
        return new NewMeshObject(this);
    }
    
    
    //TODO: Move to renderer class
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
    
    public final void calculateBounds(){
        BoundingBox boundingBox = new BoundingBox(mesh);
        this.boundingSphere = new BoundingSphere(boundingBox);
    }

    @Override
    public String getType() {
        return "MESH_OLD";
    }
    
    
    
}   
