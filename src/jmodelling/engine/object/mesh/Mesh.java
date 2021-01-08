/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import jmodelling.engine.object.mesh.edge.Edge;
import jmodelling.engine.object.mesh.face.Face;
import jmodelling.engine.object.mesh.face.Quad;
import jmodelling.engine.object.mesh.face.Tri;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.utils.ArrayUtils;

/**
 *
 * @author ANTONIO
 */
public class Mesh {

    public ArrayList<Vertex> vertices;
    public HashSet<Quad> quads;
    public HashSet<Tri> tris;
    public HashSet<Edge> edges;

    private boolean addFace(Integer... vInds) {
        //Check if indices are within the vertices list
        if(!ArrayUtils.areIndicesInRange(vertices, vInds)){
            return false;
        }
        
        //Check if indices are not duplicated
        if(ArrayUtils.hasDuplicates(vInds)){
            return false;
        }
        
        //Get vertices
        List<Vertex> subList = ArrayUtils.getSubList(vertices, vInds);
        
        //Add face
        switch (vInds.length) {
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
    
    
}
