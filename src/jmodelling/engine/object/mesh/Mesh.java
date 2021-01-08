/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import jmodelling.engine.object.mesh.edge.Edge;
import jmodelling.engine.object.mesh.face.Face;
import jmodelling.engine.object.mesh.face.Quad;
import jmodelling.engine.object.mesh.face.Tri;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.utils.ListUtils;

/**
 *
 * @author ANTONIO
 */
public class Mesh {

    public ArrayList<Vertex> vertices;
    public HashSet<Edge> edges;
    public HashSet<Quad> quads;
    public HashSet<Tri> tris;

    public Mesh() {
        vertices = new ArrayList<>();
        edges = new HashSet<>();
        quads = new HashSet<>();
        tris = new HashSet<>();

    }

    public boolean addFace(List<Integer> vInds) {
        //Check if indices are within the vertices list
        if (!ListUtils.areIndicesInRange(vertices, vInds)) {
            return false;
        }

        //Check if indices are not duplicated
        if (ListUtils.hasDuplicates(vInds)) {
            return false;
        }

        //Get vertices
        List<Vertex> subList = ListUtils.getSubList(vertices, vInds);

        //Add face
        switch (vInds.size()) {
            case Tri.N_VERTICES:
                tris.add(new Tri(subList.get(0), subList.get(1), subList.get(2)));
                break;
            case Quad.N_VERTICES:
                quads.add(new Quad(subList.get(0), subList.get(1), subList.get(2), subList.get(3)));
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean addFace(Integer... vInds) {
        return addFace(Arrays.asList(vInds));
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void removeVertices(List<Integer> indices) {
        List<Vertex> vtxsToRemove = ListUtils.getSubList(vertices, indices);

        edges.removeIf(edge -> edge.vtxs.stream().anyMatch(v -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v)));
        quads.removeIf(edge -> edge.vtxs.stream().anyMatch(v -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v)));
        tris.removeIf(edge -> edge.vtxs.stream().anyMatch(v -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v)));
        indices.stream().sorted(Comparator.reverseOrder()).mapToInt(i -> i).forEach(vertices::remove);
    }

    public void removeVertices(Integer... indices) {
        removeVertices(Arrays.asList(indices));
    }
}
