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

/**
 *
 * @author ANTONIO
 */
public class NewMeshObject extends Object3D{

    public Mesh mesh;
    public MeshGL meshGL;
    
    public NewMeshObject(Mesh mesh){
        this.mesh = mesh;
        this.meshGL = new MeshGL(mesh);
    }
    
    //TODO: Temp function
    public NewMeshObject(String objPath){
        try {
            HashMap<String, Mesh> meshes = ObjReader.readObj(objPath);
            this.mesh = meshes.get("Suzanne");
            meshGL = new MeshGL(mesh);
        } catch (IOException ex) {
            Logger.getLogger(NewMeshObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void renderOpaque(GL2 gl) {
        gl.glPushMatrix();
        gl.glMultMatrixf(getLocalAxis().toArray(), 0);
        
        meshGL.render(gl);
        
        gl.glPopMatrix();
    }
    
    
    
    /*
    public void renderShapes(GL2 gl){
        for(ShapeGL shape : meshGL.shapes.values()){
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

            gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, shape.tTris);
            gl.glColorPointer(3, GL2.GL_FLOAT, 0, shape.cTris);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, shape.nTris);
            gl.glVertexPointer(3, GL2.GL_FLOAT, 0, shape.vTris);

            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, shape.vTris.limit() / 3);

            gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
            gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        }
    }*/
    
}   
