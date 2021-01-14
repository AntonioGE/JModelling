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
import java.util.HashMap;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.shape.Shape;
import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public abstract class MeshObject extends Object3D {

    public Mesh mesh;

    public MeshObject(Mesh mesh){
        this.mesh = mesh;
    }
    
    @Override
    public void renderOpaque(GL2 gl) {
        gl.glPushMatrix();
        gl.glMultMatrixf(getLocalAxis().toArray(), 0);
        
        renderShapes(gl, mesh.tris, GL2.GL_TRIANGLES);
        renderShapes(gl, mesh.quads, GL2.GL_QUADS);
        gl.glPopMatrix();
    }

    //TODO: Remove this temporary method
    public static void renderShapes(GL2 gl, HashMap<Material, Shape> shapes, int shapeType) {

        shapes.entrySet().forEach((shape) -> {
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

            gl.glTexCoordPointer(Vertex.TEX_SIZE, GL2.GL_FLOAT, 0, shape.getValue().tCoords);
            gl.glColorPointer(Vertex.CLR_SIZE, GL2.GL_FLOAT, 0, shape.getValue().colors);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, shape.getValue().nCoords);
            gl.glVertexPointer(Vertex.POS_SIZE, GL2.GL_FLOAT, 0, shape.getValue().vCoords);

            gl.glDrawArrays(shapeType, 0, shape.getValue().vCoords.limit() / Vertex.POS_SIZE);

            gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
            gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        });

    }

}
