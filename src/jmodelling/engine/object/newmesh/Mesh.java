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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import jmodelling.engine.object.material.Material;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.ListUtils;
import jmodelling.utils.collections.IdentitySet;

/**
 *
 * @author ANTONIO
 */
public class Mesh {

    public ArrayList<Vec3f> vtxs;
    public HashMap<Edge, Edge> edges;
    public LinkedHashSet<Polygon> polys;

    public LinkedHashSet<Material> mats;

    public Mesh() {
        vtxs = new ArrayList<>();
        edges = new LinkedHashMap<>();
        polys = new LinkedHashSet<>();

        mats = new LinkedHashSet<>();
    }
    
    public Mesh(MeshGL meshGL){
        vtxs = new ArrayList<>(meshGL.vVtxs.limit() / (3 * 3));
        
        for(int i = 0; i < meshGL.vVtxs.limit(); i += 9){
            vtxs.add(new Vec3f(meshGL.vVtxs, i));
        }
        
    }

    public Mesh(Mesh other) {
        this();
        
        IdentityHashMap<Vec3f, Vec3f> vToCopy = new IdentityHashMap<>(other.vtxs.size()); 
        for(Vec3f vtx : other.vtxs){
            Vec3f vCopy = vtx.clone();
            vtxs.add(vCopy);
            vToCopy.put(vtx, vCopy);
        }
        
        IdentityHashMap<Edge, Edge> eToCopy = new IdentityHashMap<>(other.edges.size());
        for(Edge edge : other.edges.keySet()){
            Vec3f v0Copy = vToCopy.get(edge.v0);
            Vec3f v1Copy = vToCopy.get(edge.v1);
            Edge edgeCopy = new Edge(v0Copy, v1Copy);
            edges.put(edgeCopy, edgeCopy);
            eToCopy.put(edge, edgeCopy);
        }
        
        for(Polygon poly : other.polys){
            LinkedHashSet<Loop> loopsCopy = new LinkedHashSet<>(poly.loops.size());
            for(Loop loop : poly.loops){
                Vec3f vCopy = vToCopy.get(loop.vtx);
                Edge eCopy = eToCopy.get(loop.edge);
                Loop loopCopy = new Loop(vCopy, eCopy, loop.nrm.clone(), loop.clr.clone(), loop.uv.clone());
                loopsCopy.add(loopCopy);
            }
            polys.add(new Polygon(loopsCopy, poly.mat));
        }
    }

    @Override
    public Mesh clone(){
        return new Mesh(this);
    }
    
    public void addVertex(Vec3f vtx) {
        vtxs.add(vtx);
    }

    public void addEdge(int v1, int v2) {
        Edge edge = new Edge(vtxs.get(v1), vtxs.get(v2));
        edges.put(edge, edge);
    }

    public void addNewPolygon(Material mat, List<Integer> vInds, List<Vec2f> uvs, List<Vec3f> nrms, List<Vec3f> clrs) {
        if (!ListUtils.areSameSize(vInds, uvs, nrms, clrs)) {
            throw new IllegalArgumentException();
        }

        if (!ListUtils.areIndicesInRange(vtxs, vInds)) {
            throw new IllegalArgumentException();
        }

        if (ListUtils.hasDuplicatedValues(vInds)) {
            throw new IllegalArgumentException();
        }

        LinkedHashSet<Edge> newEdges = new LinkedHashSet<>(vInds.size());
        LinkedHashSet<Loop> newLoops = new LinkedHashSet<>(vInds.size());
        for (int i = 0; i < vInds.size(); i++) {
            Edge edge = new Edge(
                    vtxs.get(vInds.get(i)),
                    vtxs.get(vInds.get((i + 1) % vInds.size())));
            if(edges.containsKey(edge)){
                edge = edges.get(edges.get(edge));
            }
            Loop loop = new Loop(vtxs.get(vInds.get(i)),
                    edge, nrms.get(i), clrs.get(i), uvs.get(i));

            newEdges.add(edge);
            newLoops.add(loop);
        }

        Polygon poly = new Polygon(newLoops, mat);
        for(Edge edge : newEdges){
            edges.put(edge, edge);
        }
        polys.add(poly);
        mats.add(mat);
    }

    public HashMap<Material, Integer> getNumPolysPerMat() {
        HashMap<Material, Integer> count = new HashMap<>(mats.size());
        polys.forEach((p) -> {
            count.put(p.mat, 0);
        });
        polys.forEach((p) -> {
            count.put(p.mat, count.get(p.mat) + 1);
        });
        return count;
    }
    
    public HashMap<Material, LinkedHashSet<Polygon>> getPolysGroupedByMat(){
        HashMap<Material, Integer> nPolysPerMat = getNumPolysPerMat();
        HashMap<Material, LinkedHashSet<Polygon>> polysGrouped = new HashMap<>(nPolysPerMat.size());
        
        nPolysPerMat.entrySet().forEach((entry) -> {
            polysGrouped.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
        });
     
        polys.forEach((poly) -> {
            polysGrouped.get(poly.mat).add(poly);
        });
        return polysGrouped;
    }
    
    private static HashMap<Integer, Integer> countPolysBySize(LinkedHashSet<Polygon> polys){
        //Key is the number of sides of the polygon, value is the number of occurences
        HashMap<Integer, Integer> counts = new HashMap<>();
        for(Polygon poly : polys){
            int nSides = poly.loops.size();
            if(counts.containsKey(nSides)){
                counts.put(nSides, counts.get(nSides) + 1);
            }else{
                counts.put(nSides, 0);
            }
        }
        return counts;
    }
    
    /**
     * Groups the polygons by the number of sides
     * The result is stored in a HashMap in which the key is the number of sides
     * of the polygons, and the values are the polygons that have that number of
     * sides
     * 
     * @param polys polygons
     * @return polygons grouped by number of sides
     */
    public static HashMap<Integer, LinkedHashSet<Polygon>> groupPolysBySize(LinkedHashSet<Polygon> polys){
        HashMap<Integer, Integer> nPolysBySize = countPolysBySize(polys);
        HashMap<Integer, LinkedHashSet<Polygon>> groupedPolys = new HashMap<>(nPolysBySize.size());
        nPolysBySize.entrySet().forEach((entry) -> {
            groupedPolys.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
        });
        polys.forEach((poly) -> {
            groupedPolys.get(poly.loops.size()).add(poly);
        });
        return groupedPolys;
    }
    
    public void applyFlatShading() {
        polys.forEach((poly) -> {
            Vec3f normal = poly.getNormal();
            poly.loops.forEach((loop) -> {
                loop.nrm = normal;
            });
        });
    }

}
