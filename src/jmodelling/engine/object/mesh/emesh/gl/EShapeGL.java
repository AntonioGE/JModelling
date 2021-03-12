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
import jmodelling.engine.object.material.Material;

/**
 *
 * @author ANTONIO
 */
public class EShapeGL implements ElementGL {

    public Material mat;

    public int[] vbos;

    public int nTris;

    public FloatBuffer vtxs;
    public FloatBuffer nrms;
    public FloatBuffer clrs;
    public FloatBuffer uvs;

    private boolean updateVtxs;
    private boolean updateNrms;
    private boolean updateClrs;
    private boolean updateUvs;

    public EShapeGL(Material mat, int nTris) {
        this.mat = mat;
        this.nTris = nTris;

        vtxs = Buffers.newDirectFloatBuffer(nTris * 3 * 3);
        nrms = Buffers.newDirectFloatBuffer(nTris * 3 * 3);
        clrs = Buffers.newDirectFloatBuffer(nTris * 3 * 3);
        uvs = Buffers.newDirectFloatBuffer(nTris * 3 * 2);
    }

    @Override
    public void init(GL2 gl) {
        vbos = new int[4];

        gl.glGenBuffers(vbos.length, vbos, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, vtxs.limit() * Float.BYTES, vtxs, GL2.GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[1]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, nrms.limit() * Float.BYTES, nrms, GL2.GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[2]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, clrs.limit() * Float.BYTES, clrs, GL2.GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[3]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, uvs.limit() * Float.BYTES, uvs, GL2.GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

    }

    @Override
    public void render(GL2 gl) {
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[0]);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[1]);
        gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[2]);
        gl.glColorPointer(3, GL2.GL_FLOAT, 0, 0);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[3]);
        gl.glColorPointer(2, GL2.GL_FLOAT, 0, 0);

        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, nTris * 3);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void update(GL2 gl) {
        if (updateVtxs) {
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[0]);
            gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, vtxs.limit() * Float.BYTES, vtxs);
            updateVtxs = false;
        }

        if (updateNrms) {
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[1]);
            gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, nrms.limit() * Float.BYTES, nrms);
            updateNrms = false;
        }

        if (updateClrs) {
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[2]);
            gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, clrs.limit() * Float.BYTES, clrs);
            updateClrs = false;
        }

        if (updateUvs) {
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[3]);
            gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, uvs.limit() * Float.BYTES, uvs);
            updateUvs = false;
        }

        //gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void delete(GL2 gl) {
        vtxs.clear();
        nrms.clear();
        clrs.clear();
        uvs.clear();

        vtxs = null;
        nrms = null;
        clrs = null;
        uvs = null;
        gl.glDeleteBuffers(vbos.length, vbos, 0);
    }

    public void updateVtxs(){
        updateVtxs = true;
    }

    public void updateNrms(){
        updateNrms = true;
    }

    public void updateClrs(){
        updateClrs = true;
    }

    public void updateUvs(){
        updateUvs = true;
    }
    
}
