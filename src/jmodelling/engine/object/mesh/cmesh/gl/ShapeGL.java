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
package jmodelling.engine.object.mesh.cmesh.gl;

import com.jogamp.common.nio.Buffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import jmodelling.engine.object.material.Material;

/**
 *
 * @author ANTONIO
 */
public class ShapeGL {
    
    public Material mat;
  
    public int[] ebo;
    public int[] vbos;
    public int[] vao;
    
    public int nElements;
    
    public FloatBuffer vtxs;
    public FloatBuffer nrms;
    public FloatBuffer clrs;
    public FloatBuffer uvs;
    
    public IntBuffer elems;

    public ShapeGL(Material mat, int nNonRepeated, int nElements){
        this.mat = mat;
        this.nElements = nElements;
        
        vtxs = Buffers.newDirectFloatBuffer(nNonRepeated * 3);
        nrms = Buffers.newDirectFloatBuffer(nNonRepeated * 3);
        clrs = Buffers.newDirectFloatBuffer(nNonRepeated * 3);
        uvs = Buffers.newDirectFloatBuffer(nNonRepeated * 2);
        
        elems = Buffers.newDirectIntBuffer(nElements);
    }
    
}
