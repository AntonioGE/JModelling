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
import java.util.LinkedHashSet;
import java.util.List;
import jmodelling.engine.object.material.Material;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.ListUtils;

/**
 *
 * @author ANTONIO
 */
public class Mesh {
    
    public ArrayList<Vertex> vtxs;
    public LinkedHashSet<Edge> edges;
    public LinkedHashSet<Polygon> polys;
    
    public LinkedHashSet<Material> mats;
    
    public Mesh(){
        vtxs = new ArrayList<>();
        edges = new LinkedHashSet<>();
        polys = new LinkedHashSet<>();
        
        mats = new LinkedHashSet<>();
    }
    
    public void addVertex(Vertex vtx){
        vtxs.add(vtx);
    }
    
    public void addEdge(int v1, int v2){
        edges.add(new Edge(vtxs.get(v1), vtxs.get(v2)));
    }
    
    public void addNewPolygon(Material mat, List<Integer> vInds, List<Vec2f> uvs, List<Vec3f> nrms, List<Vec3f> clrs){
        if(!ListUtils.areSameSize(vInds, uvs, nrms, clrs)){
            throw new IllegalArgumentException();
        }
        
        if(!ListUtils.areIndicesInRange(vtxs, vInds)){
            throw new IllegalArgumentException();
        }
        
        if(ListUtils.hasDuplicatedValues(vInds)){
            throw new IllegalArgumentException();
        }
        
        LinkedHashSet<Edge> newEdges = new LinkedHashSet<>(vInds.size());
        LinkedHashSet<Loop> newLoops = new LinkedHashSet<>(vInds.size());
        for(int i = 0; i < vInds.size(); i++){
            Edge edge = new Edge(
                    vtxs.get(vInds.get(i)), 
                    vtxs.get(vInds.get((i + 1) % vInds.size())));
            Loop loop = new Loop(vtxs.get(vInds.get(i)),
                    edge, nrms.get(i), clrs.get(i), uvs.get(i));
            
            newEdges.add(edge);
            newLoops.add(loop);
        }
        
        Polygon poly = new Polygon(newLoops, mat);
        edges.addAll(newEdges);
        polys.add(poly);
        mats.add(mat);
    }
    
    public HashMap<Material, Integer> getPolysPerMat(){
        HashMap<Material, Integer> count = new HashMap<>(mats.size());
        polys.forEach((p) -> {
            count.put(p.mat, 0);
        });
        polys.forEach((p) -> {
            count.put(p.mat, count.get(p.mat) + 1);
        });
        return count;
    }
    
    public void applyFlatShading(){
        polys.forEach((poly) -> {
            Vec3f normal = poly.getNormal();
            poly.loops.forEach((loop) -> {
                loop.nrm = normal;
            });
        });
    }
    
    
}
