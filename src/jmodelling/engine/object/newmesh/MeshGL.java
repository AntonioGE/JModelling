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
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import jmodelling.engine.object.material.Material;
import jmodelling.math.vec.Vec3f;

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
        vVtxs = genVVtxs(mesh);
        cVtxs = genCVtxs(mesh);

        vEdges = genVEdges(mesh);
        cEdges = genCEdges(mesh);

        //TODO: Triangulate polygons here!!!
        shapes = genShapes(mesh);
    }

    private static HashMap<Material, ShapeGL> genShapes(Mesh mesh) {
        HashMap<Material, Integer> polysPerMat = mesh.getPolysPerMat();
        HashMap<Material, ShapeGL> shapes = new HashMap<>(polysPerMat.size());
        for (Map.Entry<Material, Integer> entry : polysPerMat.entrySet()) {
            ShapeGL shape = new ShapeGL();
            shape.mat = entry.getKey();
            shape.vTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 3);
            shape.tTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 2);
            shape.nTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 3);
            shape.cTris = Buffers.newDirectFloatBuffer(entry.getValue() * 3 * 3);
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
        mesh.edges.forEach((e) -> {
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
        mesh.edges.forEach((e) -> {
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
