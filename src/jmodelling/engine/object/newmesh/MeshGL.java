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

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import jmodelling.engine.object.cmesh.CMesh;
import jmodelling.engine.object.cmesh.CShape;
import jmodelling.engine.object.material.Material;

/**
 *
 * @author ANTONIO
 */
public class MeshGL {

    public FloatBuffer vVtxs;
    public FloatBuffer cVtxs;

    public FloatBuffer vEdges;
    public FloatBuffer cEdges;

    public HashMap<Material, ShapeGL> shapes;

    public MeshGL(Mesh mesh) {
        genData(mesh);
    }
    
    //TODO: Temp code. Move to renderer
    public void init(GL2 gl) {
        for (ShapeGL shape : shapes.values()) {
            shape.vbos = new int[4];
            gl.glGenBuffers(shape.vbos.length, shape.vbos, 0);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[0]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.vTris.limit() * Float.BYTES, shape.vTris, GL2.GL_STATIC_DRAW);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[1]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.tTris.limit() * Float.BYTES, shape.tTris, GL2.GL_STATIC_DRAW);
            
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[2]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.nTris.limit() * Float.BYTES, shape.nTris, GL2.GL_STATIC_DRAW);
            
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[3]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.cTris.limit() * Float.BYTES, shape.cTris, GL2.GL_STATIC_DRAW);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        }
    }

    public void update(GL2 gl, MeshGL mesh){
        delete(gl);
        init(gl);
    }
    
    public void delete(GL2 gl){
        shapes.values().forEach((shape) -> {
            gl.glDeleteBuffers(shape.vbos.length, shape.vbos, 0);
        });
    }
    
    //TODO: Move to renderer class
    public void render(GL2 gl) {
        for (ShapeGL shape : shapes.values()) {

            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[1]);
            gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, 0);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[3]);
            gl.glColorPointer(3, GL2.GL_FLOAT, 0, 0);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[2]);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[0]);
            gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);

            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, shape.vTris.limit() / 3);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

            gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
            gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        }
    }

    public final void genData(CMesh cmesh) {

    }

    private static FloatBuffer genVVtxs(CMesh mesh) {
        return Buffers.newDirectFloatBuffer(mesh.vtxs);
    }

    private static FloatBuffer genCVtxs(CMesh cmesh) {
        return Buffers.newDirectFloatBuffer(new float[cmesh.vtxs.length]);
    }

    private static FloatBuffer genVEdges(CMesh cmesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(cmesh.edges.length * 3);
        for (int i = 0; i < cmesh.edges.length; i++) {
            int vInd = cmesh.edges[i] * 3;
            buff.put(cmesh.vtxs[vInd]);
            buff.put(cmesh.vtxs[vInd + 1]);
            buff.put(cmesh.vtxs[vInd + 2]);
        }
        return buff;
    }

    private static FloatBuffer genCEdges(CMesh cmesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(cmesh.edges.length * 3);
        for (int i = 0; i < cmesh.edges.length; i++) {
            buff.put(0.0f);
            buff.put(0.0f);
            buff.put(0.0f);
        }
        return buff;
    }

    private static HashMap<Material, ShapeGL> genShapes(CMesh cmesh) {
        HashMap<Material, ShapeGL> shapes = new HashMap<>(cmesh.shapes.size());
        for (CShape cshape : cmesh.shapes.values()) {
            ShapeGL shapeGL = new ShapeGL();
            shapeGL.mat = cshape.mat;
            for(int i = 0; i < cshape.vtxInds.length; i++){
                
            }
            //shapeGL.vTris = cshape.
        }
        return null;
    }
    
    public final void genData(Mesh mesh){
        vVtxs = genVVtxs(mesh);
        cVtxs = genCVtxs(mesh);

        vEdges = genVEdges(mesh);
        cEdges = genCEdges(mesh);

        //TODO: Triangulate polygons here!!!
        shapes = genShapes(mesh);
    }
    
    private static HashMap<Material, ShapeGL> genShapes(Mesh mesh) {
        HashMap<Material, Integer> polysPerMat = mesh.getNumPolysPerMat();
        HashMap<Material, ShapeGL> shapes = new HashMap<>(polysPerMat.size());
        for (Map.Entry<Material, Integer> entry : polysPerMat.entrySet()) {
            ShapeGL shape = new ShapeGL();
            shape.mat = entry.getKey();
            shape.vTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 3);
            shape.tTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 2);
            shape.nTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 3);
            shape.cTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 3);
            shape.vTris.mark();
            shape.tTris.mark();
            shape.nTris.mark();
            shape.cTris.mark();
            shapes.put(shape.mat, shape);
        }

        mesh.polys.forEach((poly) -> {
            poly.loops.forEach((loop) -> {
                ShapeGL shape = shapes.get(poly.mat);
                shape.vTris.put(loop.vtx.x);
                shape.vTris.put(loop.vtx.y);
                shape.vTris.put(loop.vtx.z);

                shape.tTris.put(loop.uv.x);
                shape.tTris.put(loop.uv.y);

                shape.nTris.put(loop.nrm.x);
                shape.nTris.put(loop.nrm.y);
                shape.nTris.put(loop.nrm.z);

                shape.cTris.put(loop.clr.x);
                shape.cTris.put(loop.clr.y);
                shape.cTris.put(loop.clr.z);
            });
        });

        //Set buffer positions to 0
        for (ShapeGL shape : shapes.values()) {
            shape.vTris.reset();
            shape.tTris.reset();
            shape.nTris.reset();
            shape.cTris.reset();
        }

        return shapes;
    }

    private static FloatBuffer genVVtxs(Mesh mesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(mesh.vtxs.size() * 3);
        mesh.vtxs.forEach((v) -> {
            buff.put(v.x);
            buff.put(v.y);
            buff.put(v.z);
        });
        return buff;
    }

    private static FloatBuffer genCVtxs(Mesh mesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(mesh.vtxs.size() * 3);
        mesh.vtxs.forEach((v) -> {
            buff.put(0.0f);
            buff.put(0.0f);
            buff.put(0.0f);
        });
        return buff;
    }

    private static FloatBuffer genVEdges(Mesh mesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(mesh.edges.size() * 2 * 3);
        mesh.edges.keySet().forEach((e) -> {
            buff.put(e.v0.x);
            buff.put(e.v0.y);
            buff.put(e.v0.z);

            buff.put(e.v1.x);
            buff.put(e.v1.y);
            buff.put(e.v1.z);
        });
        return buff;
    }

    private static FloatBuffer genCEdges(Mesh mesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(mesh.edges.size() * 2 * 3);
        mesh.edges.keySet().forEach((e) -> {
            buff.put(0.0f);
            buff.put(0.0f);
            buff.put(0.0f);

            buff.put(0.0f);
            buff.put(0.0f);
            buff.put(0.0f);
        });
        return buff;
    }

}
