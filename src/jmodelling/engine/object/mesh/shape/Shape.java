/*
 * MIT License
 * 
 * Copyright (c) 2021 Antonio GE
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
            vertexOffset ++;
        }
    }
}
