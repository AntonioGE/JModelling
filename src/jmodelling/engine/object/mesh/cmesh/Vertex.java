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

import java.util.Arrays;

/**
 *
 * @author ANTONIO
 */
public class Vertex {

    public final float[] vtx;
    public final float[] nrm;
    public final float[] clr;
    public final float[] uv;

    public Vertex(float[] vtx, float[] nrm, float[] clr, float[] uv) {
        this.vtx = vtx;
        this.clr = clr;
        this.nrm = nrm;
        this.uv = uv;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Arrays.hashCode(this.vtx);
        hash = 79 * hash + Arrays.hashCode(this.nrm);
        hash = 79 * hash + Arrays.hashCode(this.clr);
        hash = 79 * hash + Arrays.hashCode(this.uv);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vertex other = (Vertex) obj;
        if (!Arrays.equals(this.vtx, other.vtx)) {
            return false;
        }
        if (!Arrays.equals(this.nrm, other.nrm)) {
            return false;
        }
        if (!Arrays.equals(this.clr, other.clr)) {
            return false;
        }
        if (!Arrays.equals(this.uv, other.uv)) {
            return false;
        }
        return true;
    }

}
