/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.edge;

import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public class Edge {
    
    public Vertex v1;
    public Vertex v2;
    
    public Edge(Vertex v1, Vertex v2){
        this.v1 = v1;
        this.v2 = v2;
    }
}
