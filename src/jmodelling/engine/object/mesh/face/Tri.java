/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.face;

import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public class Tri extends Face{
    
    public static final int N_VERTICES = 3;
    
    public Tri(Vertex v1, Vertex v2, Vertex v3) {
        super(v1, v2, v3);
    }
    
}