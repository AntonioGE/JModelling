/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.shape;

import com.jogamp.common.nio.Buffers;
import java.nio.FloatBuffer;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.face.Face;
import jmodelling.engine.object.mesh.vertex.Vertex;

/**
 *
 * @author ANTONIO
 */
public class Shape {
    
    public Material material;
    
    public FloatBuffer vCoords;
    public FloatBuffer tCoords;
    public FloatBuffer nCoords;
    public FloatBuffer colors;
    
    public Shape(Material material, int numVertices){
        this.material = material;
        vCoords = Buffers.newDirectFloatBuffer(numVertices * Vertex.POS_SIZE);
        tCoords = Buffers.newDirectFloatBuffer(numVertices * Vertex.TEX_SIZE);
        nCoords = Buffers.newDirectFloatBuffer(numVertices * Vertex.NRM_SIZE);
        colors = Buffers.newDirectFloatBuffer(numVertices * Vertex.CLR_SIZE);
    }
    
    public void writeFace(Face face, int vertexOffset){
        for(Vertex v : face.vertices){
            v.pos.writeInBuffer(vCoords, vertexOffset * Vertex.POS_SIZE);
            v.tex.writeInBuffer(tCoords, vertexOffset * Vertex.TEX_SIZE);
            v.nrm.writeInBuffer(nCoords, vertexOffset * Vertex.NRM_SIZE);
            v.clr.writeInBuffer(colors, vertexOffset * Vertex.CLR_SIZE);
        }
    }
}
