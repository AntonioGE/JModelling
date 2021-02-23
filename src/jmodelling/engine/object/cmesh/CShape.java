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
package jmodelling.engine.object.cmesh;

import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.newmesh.Vertex;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class CShape {
    
    public Material mat;
    
    public int[] edgeInds;
    public int[] vtxInds;
    public int[] nrmInds;
    public int[] clrInds;
    public int[] uvInds;
    
    public CShape(Material mat, int nLoops){
        this.mat = mat;
        edgeInds = new int[nLoops];
        vtxInds = new int[nLoops];
        nrmInds = new int[nLoops];
        clrInds = new int[nLoops];
        uvInds = new int[nLoops];
    }
    
    public CShape(CShape other){
        other.mat = mat;
        this.edgeInds = other.edgeInds.clone();
        this.vtxInds = other.vtxInds.clone();
        this.nrmInds = other.nrmInds.clone();
        this.clrInds = other.clrInds.clone();
        this.uvInds = other.uvInds.clone();
    }
    
    @Override
    public CShape clone(){
        return new CShape(this);
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
    
    public Vec3f getVCoords(CMesh cmesh, int vIndex){
        final int offset = vtxInds[vIndex] * 3;
        return new Vec3f(
                cmesh.vtxs[offset],
                cmesh.vtxs[offset + 1],
                cmesh.vtxs[offset + 2]
        );
    }
    
}
