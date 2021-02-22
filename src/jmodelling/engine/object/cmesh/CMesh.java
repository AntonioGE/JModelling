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
package jmodelling.engine.object.cmesh;

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.newmesh.Edge;
import jmodelling.engine.object.newmesh.Loop;
import jmodelling.engine.object.newmesh.Mesh;
import jmodelling.engine.object.newmesh.Polygon;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class CMesh {

    public float[] vtxs;
    public float[] nrms;
    public float[] clrs;
    public float[] uvs;

    public int[] edges;

    public HashMap<Material, CShape> shapes;

    public CMesh(Mesh mesh) {
        //Create a identity hash map for storing the vertices and their indices
        IdentityHashMap<Vec3f, Integer> vtxInds = new IdentityHashMap<>(mesh.vtxs.size());
        vtxs = new float[mesh.vtxs.size() * 3];
        for (int i = 0; i < mesh.vtxs.size(); i++) {
            Vec3f vtx = mesh.vtxs.get(i);
            vtx.writeInArray(vtxs, i * 3);
            vtxInds.put(vtx, i);
        }

        //Create a hash map for the edges and their indices
        HashMap<Edge, Integer> edgeInds = new HashMap<>(mesh.edges.size());
        edges = new int[mesh.edges.size() * 2];
        int edgeIndex = 0;
        for (Edge edge : mesh.edges.values()) {
            edgeInds.put(edge, edgeIndex);
            edges[edgeIndex * 2] = vtxInds.get(edge.v0);
            edges[edgeIndex * 2 + 1] = vtxInds.get(edge.v1);
            edgeIndex++;
        }

        //Create hash maps for the loop attributes and their indices
        HashMap<Vec3f, Integer> nrmInds = new HashMap<>(mesh.polys.size() * 3);
        HashMap<Vec3f, Integer> clrInds = new HashMap<>(mesh.polys.size() * 3);
        HashMap<Vec2f, Integer> uvInds = new HashMap<>(mesh.polys.size() * 3);
        int nrmIndex = 0;
        int clrIndex = 0;
        int uvIndex = 0;
        for (Polygon poly : mesh.polys) {
            for (Loop loop : poly.loops) {
                Vec3f nrm = loop.nrm;
                if (!nrmInds.containsKey(nrm)) {
                    nrmInds.put(nrm, nrmIndex);
                    nrmIndex++;
                }

                Vec3f clr = loop.clr;
                if (!clrInds.containsKey(clr)) {
                    clrInds.put(clr, clrIndex);
                    clrIndex++;
                }

                Vec2f uv = loop.uv;
                if (!uvInds.containsKey(uv)) {
                    uvInds.put(uv, uvIndex);
                    uvIndex++;
                }
            }
        }

        //Store the attributtes in the arrays 
        nrms = new float[nrmInds.keySet().size() * 3];
        clrs = new float[clrInds.keySet().size() * 3];
        uvs = new float[uvInds.keySet().size() * 2];
        nrmInds.entrySet().forEach((entry) -> {
            entry.getKey().writeInArray(nrms, entry.getValue() * 3);
        });
        clrInds.entrySet().forEach((entry) -> {
            entry.getKey().writeInArray(clrs, entry.getValue() * 3);
        });
        uvInds.entrySet().forEach((entry) -> {
            entry.getKey().writeInArray(uvs, entry.getValue() * 2);
        });

        //Count number of polygons per material
        HashMap<Material, Integer> polysPerMat = mesh.getPolysPerMat();
        shapes = new HashMap<>(polysPerMat.size());
        polysPerMat.entrySet().forEach((entry) -> {
            shapes.put(entry.getKey(), new CShape(entry.getKey(), entry.getValue() * 3));
        });

        //Store the attribute indices in the arrays
        HashMap<Material, Integer> loopsAddedPerMat = new HashMap<>(shapes.size());
        shapes.values().forEach((shape) -> {
            loopsAddedPerMat.put(shape.mat, 0);
        });
        mesh.polys.forEach((poly) -> {
            CShape shape = shapes.get(poly.mat);
            int vertexIndex = loopsAddedPerMat.get(poly.mat);
            for (Loop loop : poly.loops) {
                shape.vtxInds[vertexIndex] = vtxInds.get(loop.vtx);

                shape.edgeInds[vertexIndex] = edgeInds.get(loop.edge);

                shape.nrmInds[vertexIndex] = nrmInds.get(loop.nrm);
                shape.clrInds[vertexIndex] = clrInds.get(loop.clr);
                shape.uvInds[vertexIndex] = uvInds.get(loop.uv);

                vertexIndex++;
            }
            loopsAddedPerMat.put(poly.mat, vertexIndex);
        });

    }

    public CMesh(CMesh other){
        this.vtxs = other.vtxs.clone();
        this.clrs = other.clrs.clone();
        this.nrms = other.nrms.clone();
        this.uvs = other.uvs.clone();
        this.edges = other.edges.clone();
        
        this.shapes = new HashMap<>(other.shapes.size());
        other.shapes.values().forEach((shape) -> {
            shapes.put(shape.mat, shape.clone());
        });
    }
    
    @Override
    public CMesh clone(){
        return new CMesh(this);
    }
}
