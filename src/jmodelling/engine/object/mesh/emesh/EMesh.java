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
package jmodelling.engine.object.mesh.emesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.cmesh.CMesh;
import jmodelling.engine.object.mesh.cmesh.CShape;
import jmodelling.engine.object.mesh.cmesh.PolygonArray;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.CollectionUtils;
import jmodelling.utils.ListUtils;
import jmodelling.utils.collections.IdentitySet;
import jmodelling.utils.collections.node.CircularLinkedHashSet;
import jmodelling.utils.collections.node.LinkedIdentitySet;

/**
 *
 * @author ANTONIO
 */
public class EMesh {

    /**
     * Mesh data
     */
    public LinkedIdentitySet<Vec3f> vtxs;
    public LinkedHashSet<Edge> edges;
    public LinkedHashSet<Polygon> polys;
    public LinkedHashSet<Material> mats;

    /**
     * Utility data
     */
    public HashMap<Material, LinkedHashSet<Polygon>> polyGroups;
    public IdentityHashMap<Vec3f, IdentitySet<Polygon>> polysUsingVtx;

    private IdentityHashMap<Vec3f, IdentitySet<Edge>> edgesUsingVtx;
    private HashMap<Edge, IdentitySet<Polygon>> polysUsingEdge;

    /**
     * Editor data
     */
    public boolean resized;
    public boolean edited;

    public IdentitySet<Vec3f> selectedVtxs;
    public IdentitySet<Vec3f> selectedEdges;
    public IdentitySet<Vec3f> selectedPolys;

    public EMesh() {
        vtxs = new LinkedIdentitySet<>();
        edges = new LinkedHashSet<>();
        polys = new LinkedHashSet<>();

        polyGroups = new HashMap<>();

        mats = new LinkedHashSet<>();

        resized = false;
        edited = false;
        selectedVtxs = new IdentitySet<>();
        selectedEdges = new IdentitySet<>();
        selectedPolys = new IdentitySet<>();
        polysUsingVtx = new IdentityHashMap<>();
        edgesUsingVtx = new IdentityHashMap<>();
        polysUsingEdge = new HashMap<>();
    }

    public EMesh(CMesh cmesh) {
        //The vertex list is used for accessing the vertices by index
        ArrayList<Vec3f> vtxsList = new ArrayList(cmesh.vtxs.length / 3);
        vtxs = new LinkedIdentitySet<>();
        for (int i = 0; i < cmesh.vtxs.length; i += 3) {
            Vec3f vtx = new Vec3f(cmesh.vtxs, i);
            vtxs.add(vtx);
            vtxsList.add(vtx);
        }

        //The edges list is used for accessing the edges by index
        ArrayList<Edge> edgesList = new ArrayList<>(cmesh.edges.length / 2);
        edges = CollectionUtils.newLinkedHashSet(cmesh.edges.length / 2);
        for (int i = 0; i < cmesh.edges.length; i += 2) {
            final Edge edge = new Edge(vtxsList.get(cmesh.edges[i]), vtxsList.get(cmesh.edges[i + 1]));
            edges.add(edge);
            edgesList.add(edge);
        }

        polysUsingVtx = new IdentityHashMap<>();//TODO: Refactor this
        mats = CollectionUtils.newLinkedHashSet(cmesh.shapes.size());
        polys = CollectionUtils.newLinkedHashSet(cmesh.getNumPolys());
        polyGroups = CollectionUtils.newHashMap(cmesh.shapes.size());
        for (CShape shape : cmesh.shapes.values()) {
            mats.add(shape.mat);
            polyGroups.put(shape.mat, CollectionUtils.newLinkedHashSet(cmesh.getNumPolys(shape.mat)));
            for (PolygonArray pArray : shape.polys.values()) {
                final int numPolys = pArray.getNumPolygons();
                final int numLoops = pArray.nLoops;
                for (int i = 0, l = 0; i < numPolys; i++) {
                    CircularLinkedHashSet<Loop> loops = new CircularLinkedHashSet();
                    for (int j = 0; j < numLoops; j++, l++) {
                        Vec3f vtx = vtxsList.get(pArray.vtxInds[l]);
                        Edge edge = edgesList.get(pArray.edgeInds[l]);
                        Vec3f nrm = cmesh.getNrm(pArray.nrmInds[l]);
                        Vec3f clr = cmesh.getClr(pArray.clrInds[l]);
                        Vec2f uv = cmesh.getUV(pArray.uvInds[l]);
                        loops.add(new Loop(vtx, edge, nrm, clr, uv));
                    }
                    final Polygon poly = new Polygon(loops, shape.mat);
                    polys.add(poly);
                    polyGroups.get(shape.mat).add(poly);

                    //TODO: refactor this
                    for (Loop loop : poly.loops) {
                        if (!polysUsingVtx.containsKey(loop.vtx)) {
                            polysUsingVtx.put(loop.vtx, new IdentitySet<>());
                        }
                        polysUsingVtx.get(loop.vtx).add(poly);
                    }
                }
            }
        }

        resized = false;
        edited = false;
        selectedVtxs = new IdentitySet<>();
        selectedEdges = new IdentitySet<>();
        selectedPolys = new IdentitySet<>();

        genUtilityData();//TODO: Remove this
    }

    //TODO: Not tested?
    public EMesh(EMesh other) {
        this();

        IdentityHashMap<Vec3f, Vec3f> vToCopy = CollectionUtils.newIdentityHashMap(other.vtxs.size());
        for (Vec3f vtx : other.vtxs) {
            Vec3f vCopy = vtx.clone();
            vtxs.add(vCopy);
            vToCopy.put(vtx, vCopy);
        }

        IdentityHashMap<Edge, Edge> eToCopy = CollectionUtils.newIdentityHashMap(other.edges.size());
        for (Edge edge : other.edges) {
            Vec3f v0Copy = vToCopy.get(edge.v0);
            Vec3f v1Copy = vToCopy.get(edge.v1);
            Edge edgeCopy = new Edge(v0Copy, v1Copy);
            edges.add(edgeCopy);
            eToCopy.put(edge, edgeCopy);
        }

        for (Material mat : other.mats) {
            mats.add(mat);
            polyGroups.put(mat, new LinkedHashSet<>());
        }

        for (Polygon poly : other.polys) {
            CircularLinkedHashSet<Loop> loopsCopy = new CircularLinkedHashSet<>();
            for (Loop loop : poly.loops) {
                Vec3f vCopy = vToCopy.get(loop.vtx);
                Edge eCopy = eToCopy.get(loop.edge);
                Loop loopCopy = new Loop(vCopy, eCopy, loop.nrm.clone(), loop.clr.clone(), loop.uv.clone());
                loopsCopy.add(loopCopy);
            }
            Polygon polyCopy = new Polygon(loopsCopy, poly.mat);
            polys.add(polyCopy);
            polyGroups.get(poly.mat).add(polyCopy);
        }

        resized = false;
        edited = false;
    }

    @Override
    public EMesh clone() {
        return new EMesh(this);
    }

    public void addVertex(Vec3f vtx) {
        vtxs.add(vtx);
        resized = true;
    }

    public void addEdge(Vec3f v0, Vec3f v1) {
        Edge edge = new Edge(v0, v1);
        edges.add(edge);
        resized = true;
    }

    public void addNewPolygon(Material mat, Collection<Vec3f> polyVtxs, Collection<Vec2f> uvs, Collection<Vec3f> nrms, Collection<Vec3f> clrs) {
        if (!CollectionUtils.areSameSize(polyVtxs, uvs, nrms, clrs)) {
            throw new IllegalArgumentException();
        }

        if (!this.vtxs.containsAll(polyVtxs)) {
            throw new IllegalArgumentException();
        }

        if (CollectionUtils.hasDuplicates(polyVtxs)) {
            throw new IllegalArgumentException();
        }

        LinkedHashSet<Edge> newEdges = CollectionUtils.newLinkedHashSet(polyVtxs.size());
        CircularLinkedHashSet<Loop> newLoops = new CircularLinkedHashSet<>();
        ArrayList<Vec3f> vtxList = new ArrayList<>(polyVtxs);
        ArrayList<Vec3f> nrmList = new ArrayList<>(nrms);
        ArrayList<Vec3f> clrList = new ArrayList<>(clrs);
        ArrayList<Vec2f> uvList = new ArrayList<>(uvs);
        for (int i = 0; i < vtxList.size(); i++) {
            Edge edge = new Edge(vtxList.get(i), vtxList.get((i + 1) % vtxList.size()));
            Loop loop = new Loop(vtxList.get(i), edge, nrmList.get(i), clrList.get(i), uvList.get(i));

            newEdges.add(edge);
            newLoops.add(loop);
        }

        Polygon poly = new Polygon(newLoops, mat);
        if (!polys.contains(poly)) {
            for (Edge edge : newEdges) {
                edges.add(edge);
            }
            for (Loop loop : poly.loops) {
                if (!polysUsingVtx.containsKey(loop.vtx)) {
                    polysUsingVtx.put(loop.vtx, new IdentitySet<>());
                }
                polysUsingVtx.get(loop.vtx).add(poly);
            }

            polys.add(poly);
            addMaterial(mat);
            polyGroups.get(mat).add(poly);

            resized = true;
        }
    }

    public void addMaterial(Material mat) {
        if (!mats.contains(mat)) {
            mats.add(mat);
        }
        if (!polyGroups.containsKey(mat)) {
            polyGroups.put(mat, new LinkedHashSet<>());
        }
    }

    public HashMap<Material, Integer> getNumPolysPerMat() {
        HashMap<Material, Integer> count = CollectionUtils.newHashMap(mats.size());
        polys.forEach((p) -> {
            count.put(p.mat, 0);
        });
        polys.forEach((p) -> {
            count.put(p.mat, count.get(p.mat) + 1);
        });
        return count;
    }

    public HashMap<Material, LinkedHashSet<Polygon>> getPolysGroupedByMat() {
        HashMap<Material, Integer> nPolysPerMat = getNumPolysPerMat();
        HashMap<Material, LinkedHashSet<Polygon>> polysGrouped = CollectionUtils.newHashMap(nPolysPerMat.size());

        nPolysPerMat.entrySet().forEach((entry) -> {
            polysGrouped.put(entry.getKey(), CollectionUtils.newLinkedHashSet(entry.getValue()));
        });

        polys.forEach((poly) -> {
            polysGrouped.get(poly.mat).add(poly);
        });
        return polysGrouped;
    }

    private static HashMap<Integer, Integer> countPolysBySize(LinkedHashSet<Polygon> polys) {
        //Key is the number of sides of the polygon, value is the number of occurences
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (Polygon poly : polys) {
            int nSides = poly.loops.size();
            if (counts.containsKey(nSides)) {
                counts.put(nSides, counts.get(nSides) + 1);
            } else {
                counts.put(nSides, 0);
            }
        }
        return counts;
    }

    /**
     * Groups the polygons by the number of sides The result is stored in a
     * HashMap in which the key is the number of sides of the polygons, and the
     * values are the polygons that have that number of sides
     *
     * @param polys polygons
     * @return polygons grouped by number of sides
     */
    public static HashMap<Integer, LinkedHashSet<Polygon>> groupPolysBySize(LinkedHashSet<Polygon> polys) {
        HashMap<Integer, Integer> nPolysBySize = countPolysBySize(polys);
        HashMap<Integer, LinkedHashSet<Polygon>> groupedPolys = CollectionUtils.newHashMap(nPolysBySize.size());
        nPolysBySize.entrySet().forEach((entry) -> {
            groupedPolys.put(entry.getKey(), CollectionUtils.newLinkedHashSet(entry.getValue()));
        });
        polys.forEach((poly) -> {
            groupedPolys.get(poly.loops.size()).add(poly);
        });
        return groupedPolys;
    }

    public int getNumTris(Material mat) {
        LinkedHashSet<Polygon> polysWithMat = polyGroups.get(mat);
        int nVtxs = 0;
        for (Polygon poly : polysWithMat) {
            nVtxs += poly.tris.size();
        }
        return nVtxs / 3;
    }

    public void applyFlatShading() {
        polys.forEach((poly) -> {
            Vec3f normal = poly.getNormal();
            poly.loops.forEach((loop) -> {
                loop.nrm = normal;
            });
        });
    }

    //TODO: Check for another way of storing the vertex array?
    public void selectVtxs(Collection<Vec3f> vtxsToSelect) {
        for (Vec3f vtx : vtxsToSelect) {
            if (vtxs.contains(vtx)) {
                selectedVtxs.add(vtx);
            }
        }
    }

    //TODO: Check for another way of storing the vertex array?
    public void selectVtx(Vec3f vtxToSelect) {
        if (vtxs.contains(vtxToSelect)) {
            selectedVtxs.add(vtxToSelect);
        }

        //TODO: Remove this
        Set<Edge> edges = edgesUsingVtx.get(vtxToSelect);
        if (edges != null) {
            System.out.println("Vertex conected to " + edges.size() + " edges");
        }
    }

    public void deselectVtx(Vec3f vtxToDeselect) {
        selectedVtxs.remove(vtxToDeselect);
    }

    public void deselectAllVtxs() {
        selectedVtxs.clear();
    }

    public boolean isVtxSelected(Vec3f vtx) {
        return selectedVtxs.contains(vtx);
    }

    public boolean isPolygonSelected(Polygon polygon) {
        for (Loop loop : polygon.loops) {
            if (!selectedVtxs.contains(loop.vtx)) {
                return false;
            }
        }
        return true;
    }

    private Edge getOppositeEdge(Edge edge, Vec3f vtx) {
        IdentitySet<Polygon> edgePolys = polysUsingEdge.get(edge);
        //Check if the edge belongs to 1 or 2 polygons
        if (edgePolys != null && (/*edgePolys.size() == 1 ||*/edgePolys.size() == 2)) {
            IdentitySet<Edge> vtxEdges = edgesUsingVtx.get(vtx);
            //Check if the vertex is used in 4 edges
            if (vtxEdges != null && vtxEdges.size() == 4) {
                vtxEdges = new IdentitySet<>(edgesUsingVtx.get(vtx));
                vtxEdges.remove(edge);

                for (Polygon poly : edgePolys) {
                    for (Loop loop : poly.loops) {
                        vtxEdges.remove(loop.edge);
                    }
                }

                if (vtxEdges.size() == 1) {
                    return (Edge) vtxEdges.iterator().next();
                }
            }
        }
        return null;
    }

    
    private boolean getConsecutiveEdges(Edge firstEdge, Vec3f firstVtx, IdentitySet<Vec3f> consecutiveVtxs){
        Edge edge = firstEdge;
        Vec3f v = firstVtx;
        do {
            consecutiveVtxs.add(v);
            v = edge.getOther(v);
            edge = getOppositeEdge(edge, v);
        } while (edge != null && edge != firstEdge);
        return edge == firstEdge;//True if the last edge is the same as the first one (full ring)
    }
    
    public void selectEdgeLine(Edge firstEdge) {
        IdentitySet<Vec3f> vtxsToSelect = new IdentitySet<>();
        //If the consecutive edges dont form a ring, repeat on the opposite direction
        if(!getConsecutiveEdges(firstEdge, firstEdge.v0, vtxsToSelect)){
            getConsecutiveEdges(firstEdge, firstEdge.v1, vtxsToSelect);
        }
        selectedVtxs.addAll(vtxsToSelect);
    }

    public Set<Vec3f> getVtxsSet() {
        IdentitySet<Vec3f> vtxsSet = CollectionUtils.newIdentitySet(vtxs.size());
        for (Vec3f vtx : vtxs) {
            vtxsSet.add(vtx);
        }
        return vtxsSet;
    }

    private IdentityHashMap<Vec3f, IdentitySet<Edge>> genEdgesUsingVtx() {
        IdentityHashMap<Vec3f, IdentitySet<Edge>> edgesUsingVtx = CollectionUtils.newIdentityHashMap(vtxs.size());
        for (Edge edge : edges) {
            for (Vec3f vtx : edge.getVtxs()) {
                IdentitySet<Edge> edgeSet = edgesUsingVtx.get(vtx);
                if (edgeSet == null) {
                    edgeSet = new IdentitySet<>();
                    edgesUsingVtx.put(vtx, edgeSet);
                }
                edgeSet.add(edge);
            }
        }
        return edgesUsingVtx;
    }

    private HashMap<Edge, IdentitySet<Polygon>> genPolysUsingEdge() {
        HashMap<Edge, IdentitySet<Polygon>> polysUsingEdge = CollectionUtils.newHashMap(edges.size());
        for (Polygon poly : polys) {
            for (Loop loop : poly.loops) {
                IdentitySet<Polygon> polySet = polysUsingEdge.get(loop.edge);
                if (polySet == null) {
                    polySet = CollectionUtils.newIdentitySet(4);
                    polysUsingEdge.put(loop.edge, polySet);
                }
                polySet.add(poly);
            }
        }
        return polysUsingEdge;
    }

    private void genUtilityData() {
        edgesUsingVtx = genEdgesUsingVtx();
        polysUsingEdge = genPolysUsingEdge();
    }

}
