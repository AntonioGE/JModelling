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
package jmodelling.engine.object.mesh.emesh.gl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import java.nio.FloatBuffer;

/**
 *
 * @author ANTONIO
 */
public class EWireGL implements ElementGL {

    public int[] vbo;

    public int nEdges;

    public FloatBuffer vtxs;
    public FloatBuffer clrs;

    public EWireGL(int nEdges) {
        this.nEdges = nEdges;

        vtxs = Buffers.newDirectFloatBuffer(nEdges * 3 * 2);
        clrs = Buffers.newDirectFloatBuffer(nEdges * 3 * 2);
    }

    @Override
    public void init(GL2 gl) {
        vbo = new int[2];

        gl.glGenBuffers(vbo.length, vbo, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, vtxs.limit() * Float.BYTES, vtxs, GL2.GL_STATIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[1]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, clrs.limit() * Float.BYTES, clrs, GL2.GL_STATIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

        vtxs.clear();
        clrs.clear();

        vtxs = null;
        clrs = null;
    }

    @Override
    public void render(GL2 gl) {
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[1]);
        gl.glColorPointer(3, GL2.GL_FLOAT, 0, 0);
        
        gl.glDrawArrays(GL2.GL_LINES, 0, nEdges * 2);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void update(GL2 gl) {
    }

    @Override
    public void delete(GL2 gl) {
        gl.glDeleteBuffers(vbo.length, vbo, 0);
    }

}
