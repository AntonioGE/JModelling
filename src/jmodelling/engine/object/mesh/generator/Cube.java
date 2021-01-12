/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.generator;

import jmodelling.engine.object.mesh.MeshEditable;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Cube extends MeshEditable {

    public Cube() {
        super();

        /*
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
        addFace(0, 1, 5, 4);
        addFace(1, 2, 6, 5);
        addFace(2, 3, 7, 6);
        addFace(3, 0, 4, 7);
         */
        addFace(new Vertex(new Vec3f(-1.0f, -1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(1.0f, 0.0f, 0.0f)),
                new Vertex(new Vec3f(+1.0f, -1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(1.0f, 0.0f, 0.0f)),
                new Vertex(new Vec3f(+1.0f, +1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(1.0f, 0.0f, 0.0f)),
                new Vertex(new Vec3f(-1.0f, +1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(1.0f, 0.0f, 0.0f))
        );
        addFace(new Vertex(new Vec3f(-1.0f, -1.0f, 1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(0.0f, 0.0f, 1.0f)),
                new Vertex(new Vec3f(+1.0f, -1.0f, 1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(0.0f, 0.0f, 1.0f)),
                new Vertex(new Vec3f(+1.0f, +1.0f, 1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(0.0f, 0.0f, 1.0f)),
                new Vertex(new Vec3f(-1.0f, +1.0f, 1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 0.0f, -1.0f), new Vec3f(0.0f, 0.0f, 1.0f))
        );
        addFace(new Vertex(new Vec3f(-1.0f, -1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(-1.0f, 0.0f, 0.0f), new Vec3f(0.0f, 1.0f, 0.0f)),
                new Vertex(new Vec3f(-1.0f, +1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(-1.0f, 0.0f, 0.0f), new Vec3f(0.0f, 1.0f, 0.0f)),
                new Vertex(new Vec3f(-1.0f, +1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(-1.0f, 0.0f, 0.0f), new Vec3f(0.0f, 1.0f, 0.0f)),
                new Vertex(new Vec3f(-1.0f, -1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(-1.0f, 0.0f, 0.0f), new Vec3f(0.0f, 1.0f, 0.0f))
        );
        addFace(new Vertex(new Vec3f(+1.0f, -1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, -1.0f, 0.0f), new Vec3f(1.0f, 0.0f, 1.0f)),
                new Vertex(new Vec3f(+1.0f, +1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, -1.0f, 0.0f), new Vec3f(1.0f, 0.0f, 1.0f)),
                new Vertex(new Vec3f(+1.0f, +1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, -1.0f, 0.0f), new Vec3f(1.0f, 0.0f, 1.0f)),
                new Vertex(new Vec3f(+1.0f, -1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, -1.0f, 0.0f), new Vec3f(1.0f, 0.0f, 1.0f))
        );
        addFace(new Vertex(new Vec3f(-1.0f, -1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f)),
                new Vertex(new Vec3f(+1.0f, -1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f)),
                new Vertex(new Vec3f(+1.0f, -1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f)),
                new Vertex(new Vec3f(-1.0f, -1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(0.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f))
        );
        addFace(new Vertex(new Vec3f(-1.0f, +1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(1.0f, 0.0f, 0.0f), new Vec3f(1.0f, 1.0f, 1.0f)),
                new Vertex(new Vec3f(+1.0f, +1.0f, -1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(1.0f, 0.0f, 0.0f), new Vec3f(1.0f, 1.0f, 1.0f)),
                new Vertex(new Vec3f(+1.0f, +1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(1.0f, 0.0f, 0.0f), new Vec3f(1.0f, 1.0f, 1.0f)),
                new Vertex(new Vec3f(-1.0f, +1.0f, +1.0f), new Vec2f(1.0f, 1.0f), new Vec3f(1.0f, 0.0f, 0.0f), new Vec3f(1.0f, 1.0f, 1.0f))
        );
    }

}
