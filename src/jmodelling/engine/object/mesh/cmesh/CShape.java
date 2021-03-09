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

import java.util.HashMap;
import jmodelling.engine.object.material.Material;

/**
 *
 * @author ANTONIO
 */
public class CShape {
     public Material mat;
    
    public HashMap<Integer, PolygonArray> polys;
    
    public CShape(Material mat, HashMap<Integer, PolygonArray> polys){
        this.mat = mat;
        this.polys = polys;
    }
    
    public CShape(CShape other){
        mat = other.mat;
        
        polys = new HashMap<>(other.polys.size());
        other.polys.entrySet().forEach((entry)->{ 
            polys.put(entry.getKey(), entry.getValue().clone());
        });
    }
    
     @Override
    public CShape clone(){
        return new CShape(this);
    }
    
    public int getNumVertices(){
        int count = 0;
        for(PolygonArray pArray : polys.values()){
            count += pArray.vtxInds.length;
        }
        return count;
    }
    
    public int getNumTris(){
        int count = 0;
        for(PolygonArray pArray : polys.values()){
            count += pArray.getNumTriangles();
        }
        return count;
    }
    
    public int getNumPolys(){
        int count = 0;
        for(PolygonArray pArray : polys.values()){
            count += pArray.getNumPolygons();
        }
        return count;
    }
}
