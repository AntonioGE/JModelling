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
package jmodelling.engine.object.mesh.cmesh;

/**
 *
 * @author ANTONIO
 */
public class PolygonArray {
    public final int nLoops;
    
    public final int[] edgeInds;
    public final int[] vtxInds;
    public final int[] nrmInds;
    public final int[] clrInds;
    public final int[] uvInds;
    
    public final int[] tris;
    
    public PolygonArray(int nLoops, int nElements){
        this.nLoops = nLoops;
        edgeInds = new int[nLoops * nElements];
        vtxInds = new int[nLoops * nElements];
        nrmInds = new int[nLoops * nElements];
        clrInds = new int[nLoops * nElements];
        uvInds = new int[nLoops * nElements];
        
        tris = new int[((nLoops - 2) * nElements) * 3];
    }
    
    public int getNumVertices(){
        return vtxInds.length;
    }
    
    public int getNumPolygons(){
        return vtxInds.length / nLoops;
    }
    
    public int getNumTriangles(){
        return (nLoops - 2) * getNumPolygons();
    }
    
    public Vertex getVertex(CMesh cmesh, int vIndex){
        float[] vtx = new float[3];
        float[] nrm = new float[3];
        float[] clr = new float[3];
        float[] uv = new float[2];
        
        System.arraycopy(cmesh.vtxs, vtxInds[vIndex] * 3, vtx, 0, 3);
        System.arraycopy(cmesh.nrms, nrmInds[vIndex] * 3, nrm, 0, 3);
        System.arraycopy(cmesh.clrs, clrInds[vIndex] * 3, clr, 0, 3);
        System.arraycopy(cmesh.uvs, uvInds[vIndex] * 2, uv, 0, 2);
        
        return new Vertex(vtx, nrm, clr, uv);
    }
}
