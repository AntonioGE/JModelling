/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.face;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public abstract class Face {
    
    public List<Vertex> vtxs;
    
    public Face(List<Vertex> vtxs){
        this.vtxs = vtxs;
    }
    
    public Face(Vertex... vtxs){
        this(Arrays.asList(vtxs));
    }
    
    public int size(){
        return vtxs.size();
    }
    
}
