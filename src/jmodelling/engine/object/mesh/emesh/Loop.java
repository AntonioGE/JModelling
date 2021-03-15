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
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Loop {

    public Vec3f vtx;
    public Edge edge;
    //public Polygon poly;

    public Vec3f nrm;
    public Vec3f clr;
    public Vec2f uv;

    public Loop(Vec3f vtx, Edge edge, Vec3f nrm, Vec3f clr, Vec2f uv) {
        if (!edge.contains(vtx)) {
            throw new IllegalArgumentException();
        }
        this.vtx = vtx;
        this.edge = edge;
        this.nrm = nrm;
        this.clr = clr;
        this.uv = uv;
    }

    public Loop(Vec3f vtx, Edge edge) {
        this(vtx, edge,
                new Vec3f(0.0f, 0.0f, 1.0f),
                new Vec3f(1.0f, 1.0f, 1.0f),
                new Vec2f(0.0f, 0.0f));
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(vtx);
        /*
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.vtx);
        hash = 67 * hash + Objects.hashCode(this.edge);
        hash = 67 * hash + Objects.hashCode(this.nrm);
        hash = 67 * hash + Objects.hashCode(this.clr);
        hash = 67 * hash + Objects.hashCode(this.uv);
        return hash;*/
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
        final Loop other = (Loop) obj;
        return this.vtx == other.vtx;
        /*
        if (!Objects.equals(this.vtx, other.vtx)) {
            return false;
        }
        if (!Objects.equals(this.edge, other.edge)) {
            return false;
        }
        if (!Objects.equals(this.nrm, other.nrm)) {
            return false;
        }
        if (!Objects.equals(this.clr, other.clr)) {
            return false;
        }
        if (!Objects.equals(this.uv, other.uv)) {
            return false;
        }
        return true;*/
    }
}
