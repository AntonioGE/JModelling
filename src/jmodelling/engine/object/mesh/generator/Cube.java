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
