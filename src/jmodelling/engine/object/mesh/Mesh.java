/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh;

import java.nio.FloatBuffer;
import java.util.HashMap;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.shape.Shape;

/**
 *
 * @author ANTONIO
 */
public class Mesh {
    
    public HashMap<String, Material> materials;
    public HashMap<Material, Shape> tris;
    public HashMap<Material, Shape> quads;
    
    public FloatBuffer vEdges;
    public FloatBuffer cEdges;
    
    public FloatBuffer vVertices;
    public FloatBuffer cVertices;
    
    public Mesh(HashMap<Material, Shape> tris, HashMap<Material, Shape> quads){
        this.tris = tris;
        this.quads = quads;
    }
    
}
