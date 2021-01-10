/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    @Override
    public void renderOpaque(GL2 gl) {
        renderShapes(gl, mesh.tris, GL2.GL_TRIANGLES);
        renderShapes(gl, mesh.quads, GL2.GL_QUADS);
    }

    //TODO: Remove this temporary method
    public static void renderShapes(GL2 gl, HashMap<Material, Shape> shapes, int shapeType) {

        shapes.entrySet().forEach((shape) -> {
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

            gl.glTexCoordPointer(Vertex.TEX_SIZE, GL2.GL_FLOAT, 0, shape.getValue().tCoords);
            gl.glColorPointer(Vertex.CLR_SIZE, GL2.GL_FLOAT, 0, shape.getValue().colors);
            gl.glVertexPointer(Vertex.POS_SIZE, GL2.GL_FLOAT, 0, shape.getValue().vCoords);

            gl.glDrawArrays(shapeType, 0, shape.getValue().vCoords.limit() / Vertex.POS_SIZE);

            gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        });

    }

}
