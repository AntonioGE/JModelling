/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh;

import java.util.ArrayList;
import java.util.HashSet;
import jmodelling.engine.object.mesh.edge.Edge;
import jmodelling.engine.object.mesh.face.Quad;
import jmodelling.engine.object.mesh.face.Tri;
import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public class Mesh {
    
    public ArrayList<Vertex> vertices;
    public HashSet<Quad> quads;
    public HashSet<Tri> tris;
    public HashSet<Edge> edges;
}
