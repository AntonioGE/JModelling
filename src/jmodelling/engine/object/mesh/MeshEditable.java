/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.edge.Edge;
import jmodelling.engine.object.mesh.face.Quad;
import jmodelling.engine.object.mesh.face.Tri;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.utils.ListUtils;

/**
 *
 * @author ANTONIO
 */
public class MeshEditable {

    protected ArrayList<Vertex> vertices;
    protected HashSet<Edge> edges;
    protected HashSet<Quad> quads;
    protected HashSet<Tri> tris;
    protected HashMap<String, Material> materials;

    public MeshEditable() {
        vertices = new ArrayList<>();
        edges = new HashSet<>();
        quads = new HashSet<>();
        tris = new HashSet<>();

    }
    
    public boolean addFace(List<Vertex> vertices) {
        //Add face
        switch (vertices.size()) {
            case Tri.N_VERTICES:
                tris.add(new Tri(vertices.get(0), vertices.get(1), vertices.get(2)));
                break;
            case Quad.N_VERTICES:
                quads.add(new Quad(vertices.get(0), vertices.get(1), vertices.get(2), vertices.get(3)));
                break;
            default:
                return false;
        }

        //Add edges
        for (int i = 0; i < vertices.size(); i++) {
            edges.add(new Edge(vertices.get(i), vertices.get(i % vertices.size())));
        }

        return true;
    }

    public boolean addFace(Vertex... vertices) {
        return addFace(Arrays.asList(vertices));
    }

    public boolean addFaceInds(List<Integer> vInds) {
        //Check if indices are within the vertices list
        if (!ListUtils.areIndicesInRange(vertices, vInds)) {
            return false;
        }

        //Check if indices are not duplicated
        if (ListUtils.hasDuplicates(vInds)) {
            return false;
        }

        //Get vertices that form the face
        List<Vertex> subList = ListUtils.getSubList(vertices, vInds);

        //Add face
        return addFace(subList);
    }

    public boolean addFace(Integer... vInds) {
        return addFaceInds(Arrays.asList(vInds));
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addVertices(List<Vertex> vertices) {
        vertices.addAll(vertices);
    }

    public void addVertices(Vertex... vertices) {
        addVertices(Arrays.asList(vertices));
    }

    public void removeVertices(List<Integer> indices) {
        List<Vertex> vtxsToRemove = ListUtils.getSubList(vertices, indices);

        edges.removeIf(edge -> edge.vtxs.stream().anyMatch(v1 -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v1)));
        quads.removeIf(edge -> edge.vtxs.stream().anyMatch(v1 -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v1)));
        tris.removeIf(edge -> edge.vtxs.stream().anyMatch(v1 -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v1)));
        indices.stream().sorted(Comparator.reverseOrder()).mapToInt(i -> i).forEach(vertices::remove);
    }

    public void removeVertices(Integer... indices) {
        removeVertices(Arrays.asList(indices));
    }
}
