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
package jmodelling.engine.object.cmesh2;

import java.util.Collection;
import jmodelling.engine.object.newmesh.Polygon;

/**
 *
 * @author ANTONIO
 */
public class PolygonArray {
    
    public final int nLoops;
    
    public int[] edgeInds;
    public int[] vtxInds;
    public int[] nrmInds;
    public int[] clrInds;
    public int[] uvInds;
    
    public PolygonArray(int nLoops, int nElements){
        this.nLoops = nLoops;
        edgeInds = new int[nLoops * nElements];
        vtxInds = new int[nLoops * nElements];
        nrmInds = new int[nLoops * nElements];
        clrInds = new int[nLoops * nElements];
        uvInds = new int[nLoops * nElements];
    }
    
    public static void triangulate(float[] vtxs, int[] vInds){
        
    }
    
}
