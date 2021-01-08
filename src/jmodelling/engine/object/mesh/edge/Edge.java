/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public class Edge {
    
    public List<Vertex> vtxs;
    
    public Edge(List<Vertex> vtxs){
        this.vtxs = vtxs;
    }
    
    public Edge(Vertex... vtxs){
        this(Arrays.asList(vtxs));
    }
}
