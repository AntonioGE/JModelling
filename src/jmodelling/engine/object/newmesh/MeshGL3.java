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
import jmodelling.engine.object.cmesh2.CMesh2;
import jmodelling.engine.object.cmesh2.CShape2;
import jmodelling.engine.object.cmesh2.PolygonArray;
import jmodelling.engine.object.material.Material;

/**
 *
 * @author ANTONIO
 */
public class MeshGL3 {

    public FloatBuffer vVtxs;
    //public FloatBuffer cVtxs;

    public FloatBuffer vEdges;
    //public FloatBuffer cEdges;

    public HashMap<Material, ShapeGL2> shapes;

    public MeshGL3(CMesh2 cmesh) {
        genData(cmesh);
    }

    //TODO: Move this?
    public void init(GL2 gl) {
        for (ShapeGL2 shape : shapes.values()) {
            shape.vbos = new int[4];
            shape.ebo = new int[1];
            shape.vao = new int[1];

            gl.glGenVertexArrays(shape.vao.length, shape.vao, 0);
            gl.glGenBuffers(shape.vbos.length, shape.vbos, 0);
            gl.glGenBuffers(shape.ebo.length, shape.ebo, 0);

            gl.glBindVertexArray(shape.vao[0]);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[0]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.vtxs.limit() * Float.BYTES, shape.vtxs, GL2.GL_STATIC_DRAW);
            gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[1]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.nrms.limit() * Float.BYTES, shape.nrms, GL2.GL_STATIC_DRAW);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, 0);
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[2]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.clrs.limit() * Float.BYTES, shape.clrs, GL2.GL_STATIC_DRAW);
            gl.glColorPointer(3, GL2.GL_FLOAT, 0, 0);
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, shape.vbos[3]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, shape.uvs.limit() * Float.BYTES, shape.uvs, GL2.GL_STATIC_DRAW);
            gl.glColorPointer(2, GL2.GL_FLOAT, 0, 0);
            gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, shape.ebo[0]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, shape.elems.limit() * Integer.BYTES, shape.elems, GL2.GL_STATIC_DRAW);

            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

            //Free buffers after uploading to graphics card
            //TODO: Is this needed?
            shape.vtxs.clear();
            shape.nrms.clear();
            shape.clrs.clear();
            shape.uvs.clear();
            shape.elems.clear();

            shape.vtxs = null;
            shape.nrms = null;
            shape.clrs = null;
            shape.uvs = null;
            shape.elems = null;
        }
    }

    public void update(GL2 gl, MeshGL3 mesh) {
        //delete(gl);
        //init(gl);
    }

    public void delete(GL2 gl) {
        shapes.values().forEach((shape) -> {
            gl.glDeleteVertexArrays(shape.vao.length, shape.vao, 0);
            gl.glDeleteBuffers(shape.vbos.length, shape.vbos, 0);
            gl.glDeleteBuffers(shape.ebo.length, shape.ebo, 0);
        });
    }

    //TODO: Move to renderer class
    public void render(GL2 gl) {
        for (ShapeGL2 shape : shapes.values()) {

            gl.glBindVertexArray(shape.vao[0]);

            gl.glDrawElements(GL2.GL_TRIANGLES, shape.nElements, GL2.GL_UNSIGNED_INT, 0);

            gl.glBindVertexArray(0);
        }
    }

    public final void genData(CMesh2 cmesh) {
        vVtxs = genVVtxs(cmesh);
        //cVtxs = genCVtxs(cmesh);

        vEdges = genVEdges(cmesh);
        //cEdges = genCEdges(cmesh);

        shapes = genShapes(cmesh);
    }

    private static FloatBuffer genVVtxs(CMesh2 cmesh) {
        return Buffers.newDirectFloatBuffer(cmesh.vtxs);
    }

    private static FloatBuffer genCVtxs(CMesh2 cmesh) {
        return Buffers.newDirectFloatBuffer(new float[cmesh.vtxs.length]);
    }

    private static FloatBuffer genVEdges(CMesh2 cmesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(cmesh.edges.length * 3);
        buff.mark();
        for (int i = 0; i < cmesh.edges.length; i++) {
            int vInd = cmesh.edges[i] * 3;
            buff.put(cmesh.vtxs[vInd]);
            buff.put(cmesh.vtxs[vInd + 1]);
            buff.put(cmesh.vtxs[vInd + 2]);
        }
        buff.reset();
        return buff;
    }

    private static FloatBuffer genCEdges(CMesh2 cmesh) {
        FloatBuffer buff = Buffers.newDirectFloatBuffer(cmesh.edges.length * 3);
        buff.mark();
        for (int i = 0; i < cmesh.edges.length; i++) {
            buff.put(0.0f);
            buff.put(0.0f);
            buff.put(0.0f);
        }
        buff.reset();
        return buff;
    }

    private static HashMap<Material, ShapeGL2> genShapes(CMesh2 cmesh) {
        HashMap<Material, ShapeGL2> shapes = new HashMap<>(cmesh.shapes.size());

        cmesh.shapes.values().forEach((shape) -> {
            /**
             * Create a hash map for storing the vertices as keys and their indices
             * as values.
             */
            
            HashMap<Vertex, Integer> vertices = new HashMap<>(shape.getNumVertices());
            int[] vInds = new int[shape.getNumTris() * 3];
            int vtxsAdded = 0;
            int vIndOffset = 0;
            for (PolygonArray pArray : shape.polys.values()) {
                for(int i = 0; i < pArray.tris.length; i++, vIndOffset++){
                    Vertex vtx = pArray.getVertex(cmesh, pArray.tris[i]);
                    Integer index = vertices.get(vtx);
                    if (index == null) {
                        vertices.put(vtx, vtxsAdded);
                        vInds[vIndOffset] = vtxsAdded;
                        vtxsAdded++;
                    } else {
                        vInds[vIndOffset] = index;
                    }
                }
            }
            
            //Create the shape for OpenGL and initialize the buffers
            ShapeGL2 shapeGL = new ShapeGL2(shape.mat, vertices.size(), vInds.length);
            shapeGL.vtxs.mark();
            shapeGL.nrms.mark();
            shapeGL.clrs.mark();
            shapeGL.uvs.mark();
            shapeGL.elems.mark();

            //Put all the vertex data
            for (Map.Entry<Vertex, Integer> entry : vertices.entrySet()) {
                Vertex vtx = entry.getKey();
                int index = entry.getValue();
                shapeGL.vtxs.position(index * 3);
                shapeGL.nrms.position(index * 3);
                shapeGL.clrs.position(index * 3);
                shapeGL.uvs.position(index * 2);

                shapeGL.vtxs.put(vtx.vtx);
                shapeGL.nrms.put(vtx.nrm);
                shapeGL.clrs.put(vtx.clr);
                shapeGL.uvs.put(vtx.uv);
            }
            
            //Put all the elements data
            shapeGL.elems.put(vInds);

            //Reset the position of the buffers
            shapeGL.vtxs.reset();
            shapeGL.nrms.reset();
            shapeGL.clrs.reset();
            shapeGL.uvs.reset();
            shapeGL.elems.reset();

            //Put the new shapeGL into the map
            shapes.put(shapeGL.mat, shapeGL);
            
        });
        return shapes;
    }

}
