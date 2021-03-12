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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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

/**
 *
 * @author ANTONIO
 */
public class EMesh {

    /**
     * Mesh data
     */
    public ArrayList<Vec3f> vtxs;
    public HashMap<Edge, Edge> edges;
    public LinkedHashSet<Polygon> polys;

    public HashMap<Material, LinkedHashSet<Polygon>> polyGroups;
    public IdentityHashMap<Vec3f, IdentitySet<Polygon>> polysUsingVtx;

    public LinkedHashSet<Material> mats;

    /**
     * Editor data
     */
    public boolean resized;
    public boolean edited;

    public IdentitySet<Vec3f> selectedVtxs;
    public IdentitySet<Vec3f> selectedEdges;
    public IdentitySet<Vec3f> selectedPolys;

    public EMesh() {
        vtxs = new ArrayList<>();
        edges = new LinkedHashMap<>();
        polys = new LinkedHashSet<>();

        polyGroups = new HashMap<>();

        mats = new LinkedHashSet<>();

        resized = false;
        edited = false;
        selectedVtxs = new IdentitySet<>();
        selectedEdges = new IdentitySet<>();
        selectedPolys = new IdentitySet<>();
        polysUsingVtx = new IdentityHashMap<>();
    }

    public EMesh(CMesh cmesh) {
        vtxs = new ArrayList<>(cmesh.vtxs.length / 3);
        for (int i = 0; i < cmesh.vtxs.length; i += 3) {
            vtxs.add(new Vec3f(cmesh.vtxs, i));
        }

        //The edges list is used for accessing the edges by index
        ArrayList<Edge> edgesList = new ArrayList<>(cmesh.edges.length / 2);
        edges = CollectionUtils.newHashMap(cmesh.edges.length / 2);
        for (int i = 0; i < cmesh.edges.length; i += 2) {
            final Edge edge = new Edge(vtxs.get(cmesh.edges[i]), vtxs.get(cmesh.edges[i + 1]));
            edges.put(edge, edge);
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
                        Vec3f vtx = vtxs.get(pArray.vtxInds[l]);
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
        for (Edge edge : other.edges.keySet()) {
            Vec3f v0Copy = vToCopy.get(edge.v0);
            Vec3f v1Copy = vToCopy.get(edge.v1);
            Edge edgeCopy = new Edge(v0Copy, v1Copy);
            edges.put(edgeCopy, edgeCopy);
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

    public void addEdge(int v1, int v2) {
        Edge edge = new Edge(vtxs.get(v1), vtxs.get(v2));
        if (!edges.containsKey(edge)) {
            edges.put(edge, edge);
            resized = true;
        }
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

        LinkedHashSet<Edge> newEdges = CollectionUtils.newLinkedHashSet(vInds.size());
        CircularLinkedHashSet<Loop> newLoops = new CircularLinkedHashSet<>();
        for (int i = 0; i < vInds.size(); i++) {
            Edge edge = new Edge(
                    vtxs.get(vInds.get(i)),
                    vtxs.get(vInds.get((i + 1) % vInds.size())));
            if (edges.containsKey(edge)) {
                edge = edges.get(edges.get(edge));
            }
            Loop loop = new Loop(vtxs.get(vInds.get(i)),
                    edge, nrms.get(i), clrs.get(i), uvs.get(i));

            newEdges.add(edge);
            newLoops.add(loop);
        }

        Polygon poly = new Polygon(newLoops, mat);
        for (Edge edge : newEdges) {
            edges.put(edge, edge);
        }
        for (Loop loop : poly.loops) {
            if (!polysUsingVtx.containsKey(loop.vtx)) {
                polysUsingVtx.put(loop.vtx, new IdentitySet<>());
            }
            polysUsingVtx.get(loop.vtx).add(poly);
        }
        if (!polys.contains(poly)) {
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
        IdentitySet<Vec3f> vtxsSet = CollectionUtils.newIdentitySet(vtxs.size());
        for (Vec3f vtx : vtxs) {
            vtxsSet.add(vtx);
        }
        for (Vec3f vtx : vtxsToSelect) {
            if (vtxsSet.contains(vtx)) {
                selectedVtxs.add(vtx);
            }
        }
    }

    //TODO: Check for another way of storing the vertex array?
    public void selectVtx(Vec3f vtxToSelect) {
        IdentitySet<Vec3f> vtxsSet = CollectionUtils.newIdentitySet(vtxs.size());
        for (Vec3f vtx : vtxs) {
            vtxsSet.add(vtx);
        }
        if (vtxsSet.contains(vtxToSelect)) {
            selectedVtxs.add(vtxToSelect);
        }
    }

    public boolean isPolygonSelected(Polygon polygon) {
        for (Loop loop : polygon.loops) {
            if (!selectedVtxs.contains(loop.vtx)) {
                return false;
            }
        }
        return true;
    }

}
