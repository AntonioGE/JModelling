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
package jmodelling.engine.object.mesh.emesh;

import java.util.Objects;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Edge {

    public final Vec3f v0;
    public final Vec3f v1;

    public Edge(Vec3f v0, Vec3f v1) {
        if (v0 == v1) {
            throw new IllegalArgumentException();
        }
        this.v0 = v0;
        this.v1 = v1;
    }

    public boolean contains(Vec3f vtx) {
        return vtx == v0 || vtx == v1;
    }

    @Override
    public int hashCode() {
        //return Objects.hashCode(this.v0) ^ Objects.hashCode(this.v1);
        return System.identityHashCode(v0) ^ System.identityHashCode(v1);
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
        final Edge other = (Edge) obj;

        return ((this.v0 == other.v0) && (this.v1 == other.v1))
                || ((this.v0 == other.v1) && (this.v1 == other.v0));
    }

    public Vec3f[] getVtxs() {
        return new Vec3f[]{v0, v1};
    }
    
    public Vec3f getOther(Vec3f v){
        if(v == v0){
            return v1;
        }else{
            return v0;
        }
    }
}
