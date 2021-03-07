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
package jmodelling.engine.object.mesh.cmesh;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.utils.triangulator.EarClipping;
import jmodelling.engine.object.mesh.emesh.EMesh;
import jmodelling.engine.object.mesh.emesh.Edge;
import jmodelling.engine.object.mesh.emesh.Loop;
import jmodelling.engine.object.mesh.emesh.Polygon;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class CMesh {
    public final float[] vtxs;
    public final float[] nrms;
    public final float[] clrs;
    public final float[] uvs;

    public final int[] edges;
    
    public final HashMap<Material, CShape> shapes;
    
    public CMesh(EMesh mesh) {
        /**
         * STEP 1: Create HashMaps.
         *
         * Create hashmaps for storing the vertex data without repetitions and
         * assigning a index to each of the vertices
         */

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

        /**
         * STEP 2: Generate the shapes.
         *
         * Convert the mesh polygons into shapes grouped by materials. Each
         * shape is made of polygon arrays grouped by the number of loops
         */
        HashMap<Material, LinkedHashSet<Polygon>> matPolys = mesh.getPolysGroupedByMat();
        shapes = new HashMap<>(matPolys.size());
        matPolys.entrySet().forEach((mpEntry) -> {
            HashMap<Integer, LinkedHashSet<Polygon>> sizePolys = EMesh.groupPolysBySize(mpEntry.getValue());
            HashMap<Integer, PolygonArray> polyArrays = new HashMap<>(sizePolys.size());
            for (Map.Entry<Integer, LinkedHashSet<Polygon>> spEntry : sizePolys.entrySet()) {
                PolygonArray pArray = new PolygonArray(spEntry.getKey(), spEntry.getValue().size());
                int vertexIndex = 0;
                int triIndex = 0;
                int polyIndex = 0;
                for (Polygon poly : spEntry.getValue()) {
                    for (Loop loop : poly.loops) {
                        pArray.vtxInds[vertexIndex] = vtxInds.get(loop.vtx);

                        pArray.edgeInds[vertexIndex] = edgeInds.get(loop.edge);

                        pArray.nrmInds[vertexIndex] = nrmInds.get(loop.nrm);
                        pArray.clrInds[vertexIndex] = clrInds.get(loop.clr);
                        pArray.uvInds[vertexIndex] = uvInds.get(loop.uv);

                        vertexIndex++;
                    }

                    /*
                    List<Vec3f> polyVtxs = poly.getVertices();
                    List<Integer> indices = EarClipping.triangulate(polyVtxs);
                    for (Integer i : indices) {
                        pArray.tris[triIndex] = vtxInds.get(polyVtxs.get(i));
                        triIndex++;
                    }*/
                    List<Vec3f> polyVtxs = poly.getVertices();
                    List<Integer> indices = EarClipping.triangulate(polyVtxs);
                    for (Integer i : indices) {
                        pArray.tris[triIndex] = polyIndex + i;
                        triIndex++;
                    }
                    polyIndex += poly.loops.size();
                }
                polyArrays.put(pArray.nLoops, pArray);
            }
            shapes.put(mpEntry.getKey(), new CShape(mpEntry.getKey(), polyArrays));
        });
    }
    
    public CMesh(CMesh other){
        vtxs = other.vtxs.clone();
        nrms = other.nrms.clone();
        clrs = other.clrs.clone();
        uvs = other.uvs.clone();
        
        edges = other.edges.clone();
        
        shapes = new HashMap<>(other.shapes.size());
        other.shapes.entrySet().forEach((entry)->{
            shapes.put(entry.getKey(), entry.getValue().clone());
        });
    }
    
    @Override
    public CMesh clone(){
        return new CMesh(this);
    }
}
