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
package jmodelling.engine.object.mesh.cmesh.gl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author ANTONIO
 */
public class CWireGL {

    public int[] ebo;
    public int[] vbo;

    public int nElements;

    public FloatBuffer vtxs;

    public IntBuffer elems;

    public CWireGL(int nVertices, int nElements) {
        this.nElements = nElements;

        vtxs = Buffers.newDirectFloatBuffer(nVertices * 3);

        elems = Buffers.newDirectIntBuffer(nElements);
    }

    public CWireGL(float[] vertices, int[] edgeIndices) {
        this.nElements = edgeIndices.length;

        vtxs = Buffers.newDirectFloatBuffer(vertices);

        elems = Buffers.newDirectIntBuffer(edgeIndices);
    }

    public void init(GL2 gl) {
        vbo = new int[1];
        ebo = new int[1];

        gl.glGenBuffers(vbo.length, vbo, 0);
        gl.glGenBuffers(ebo.length, ebo, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, vtxs.limit() * Float.BYTES, vtxs, GL2.GL_STATIC_DRAW);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, elems.limit() * Integer.BYTES, elems, GL2.GL_STATIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

        vtxs.clear();
        elems.clear();

        vtxs = null;
        elems = null;
    }

    public void render(GL2 gl) {
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
        gl.glDrawElements(GL2.GL_LINES, nElements, GL2.GL_UNSIGNED_INT, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }

    public void delete(GL2 gl) {
        if (vbo != null) {
            gl.glDeleteBuffers(vbo.length, vbo, 0);
        }
        if (ebo != null) {
            gl.glDeleteBuffers(ebo.length, ebo, 0);
        }
    }

}
