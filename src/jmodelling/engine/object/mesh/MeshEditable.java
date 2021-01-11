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
import jmodelling.engine.object.mesh.face.Face;
import jmodelling.engine.object.mesh.face.Quad;
import jmodelling.engine.object.mesh.face.Tri;
import jmodelling.engine.object.mesh.shape.Shape;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.utils.ListUtils;

/**
 *
 * @author ANTONIO
 */
public class MeshEditable {

    protected ArrayList<Vertex> vertices;
    protected HashSet<Edge> edges;
    protected HashSet<Tri> tris;
    protected HashSet<Quad> quads;
    protected HashMap<String, Material> materials;

    public MeshEditable() {
        vertices = new ArrayList<>();
        edges = new HashSet<>();
        quads = new HashSet<>();
        tris = new HashSet<>();

    }
    
    public static HashSet<Material> getMaterialsFromFaceSet(HashSet<Face> faces){
        HashSet<Material> materials = new HashSet<>();
        faces.forEach((face) -> {
            materials.add(face.material);
        });
        return materials;
    }
    
    
    public static HashMap<Material, Shape> facesToShapes(HashSet<Face> faces){
        //Get all the materials from the set of faces
        HashSet<Material> materials = getMaterialsFromFaceSet(faces);
        
        //Count number of vertices per material
        HashMap<Material, Integer> vPerMat = new HashMap<>(materials.size());
        materials.forEach((material) -> {
            vPerMat.put(material, 0);
        });
        
        faces.forEach((face) ->{
            vPerMat.put(face.material, vPerMat.get(face.material) + face.size());
        });
        
        //Create the shapes
        HashMap<Material, Shape> shapes = new HashMap<>(materials.size());
        vPerMat.entrySet().forEach((v) -> {
            shapes.put(v.getKey(), new Shape(v.getKey(), v.getValue()));
        });
        
        //Create offsets for each shape
        HashMap<Material, Integer> offsets = new HashMap<>(materials.size());
        materials.forEach((material) -> {
            offsets.put(material, 0);
        });
        
        //Set the data into the shapes
        faces.forEach((face)->{
            shapes.get(face.material).writeFace(face, offsets.get(face.material));
            offsets.put(face.material, offsets.get(face.material) + face.size());
        });
        
        return shapes;
    }
    
    public Mesh toMesh(){
        HashMap<Material, Shape> triShapes = facesToShapes((HashSet<Face>)(HashSet<?>)tris);
        HashMap<Material, Shape> quadShapes = facesToShapes((HashSet<Face>)(HashSet<?>)quads);
        
        return new Mesh(triShapes, quadShapes);
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
        this.vertices.addAll(vertices);
    }

    public void addVertices(Vertex... vertices) {
        addVertices(Arrays.asList(vertices));
    }

    public void removeVertices(List<Integer> indices) {
        List<Vertex> vtxsToRemove = ListUtils.getSubList(vertices, indices);

        edges.removeIf(edge -> edge.vtxs.stream().anyMatch(v1 -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v1)));
        quads.removeIf(edge -> edge.vertices.stream().anyMatch(v1 -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v1)));
        tris.removeIf(edge -> edge.vertices.stream().anyMatch(v1 -> vtxsToRemove.stream().anyMatch(v2 -> v2 == v1)));
        indices.stream().sorted(Comparator.reverseOrder()).mapToInt(i -> i).forEach(vertices::remove);
    }

    public void removeVertices(Integer... indices) {
        removeVertices(Arrays.asList(indices));
    }
}
