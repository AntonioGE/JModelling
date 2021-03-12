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
import jmodelling.engine.object.material.Material;

/**
 *
 * @author ANTONIO
 */
public class CShapeGL {

    public Material mat;

    public int[] ebo;
    public int[] vbos;
    //public int[] vao;

    public int nElements;

    public FloatBuffer vtxs;
    public FloatBuffer nrms;
    public FloatBuffer clrs;
    public FloatBuffer uvs;

    public IntBuffer elems;

    public CShapeGL(Material mat, int nNonRepeated, int nElements) {
        this.mat = mat;
        this.nElements = nElements;

        vtxs = Buffers.newDirectFloatBuffer(nNonRepeated * 3);
        nrms = Buffers.newDirectFloatBuffer(nNonRepeated * 3);
        clrs = Buffers.newDirectFloatBuffer(nNonRepeated * 3);
        uvs = Buffers.newDirectFloatBuffer(nNonRepeated * 2);

        elems = Buffers.newDirectIntBuffer(nElements);
    }

    public void init(GL2 gl) {
        vbos = new int[4];
        ebo = new int[1];

        gl.glGenBuffers(vbos.length, vbos, 0);
        gl.glGenBuffers(ebo.length, ebo, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, vtxs.limit() * Float.BYTES, vtxs, GL2.GL_STATIC_DRAW);
        //gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[1]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, nrms.limit() * Float.BYTES, nrms, GL2.GL_STATIC_DRAW);
        //gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[2]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, clrs.limit() * Float.BYTES, clrs, GL2.GL_STATIC_DRAW);
        //gl.glColorPointer(3, GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[3]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, uvs.limit() * Float.BYTES, uvs, GL2.GL_STATIC_DRAW);
        //gl.glColorPointer(2, GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, elems.limit() * Integer.BYTES, elems, GL2.GL_STATIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

        //Free buffers after uploading to graphics card
        //TODO: Is this needed?
        vtxs.clear();
        nrms.clear();
        clrs.clear();
        uvs.clear();
        elems.clear();

        vtxs = null;
        nrms = null;
        clrs = null;
        uvs = null;
        elems = null;
    }

    public void render(GL2 gl) {
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[0]);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[1]);
        gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[2]);
        gl.glColorPointer(3, GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[3]);
        gl.glColorPointer(2, GL2.GL_FLOAT, 0, 0);
        //gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
        gl.glDrawElements(GL2.GL_TRIANGLES, nElements, GL2.GL_UNSIGNED_INT, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

        //gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        //gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        //gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        //gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    }

    public void delete(GL2 gl) {
        //gl.glDeleteVertexArrays(vao.length, vao, 0);
        if(vbos != null){
            gl.glDeleteBuffers(vbos.length, vbos, 0);
        }
        if(ebo != null){
            gl.glDeleteBuffers(ebo.length, ebo, 0);
        }
    }

}
