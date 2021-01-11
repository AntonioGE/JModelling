/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.generator;

import jmodelling.engine.object.mesh.MeshEditable;
import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public class Cube extends MeshEditable {

    public Cube() {
        super();
        addVertices(
                new Vertex(-1.0f, -1.0f, -1.0f),
                new Vertex(+1.0f, -1.0f, -1.0f),
                new Vertex(+1.0f, +1.0f, -1.0f),
                new Vertex(-1.0f, +1.0f, -1.0f),
                new Vertex(-1.0f, -1.0f, +1.0f),
                new Vertex(+1.0f, -1.0f, +1.0f),
                new Vertex(+1.0f, +1.0f, +1.0f),
                new Vertex(-1.0f, +1.0f, +1.0f)
        );
        
        addFace(0, 1, 2, 3);
        addFace(4, 5, 6, 7);
    }

}
