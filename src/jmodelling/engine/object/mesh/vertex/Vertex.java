/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.vertex;

import java.nio.FloatBuffer;
import java.util.Objects;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Vertex {

    public static final int POS_SIZE = 3;
    public static final int TEX_SIZE = 2;
    public static final int NRM_SIZE = 3;
    public static final int CLR_SIZE = 3;
    
    public Vec3f pos;
    public Vec2f tex;
    public Vec3f nrm;
    public Vec3f clr;

    public Vertex(Vec3f pos) {
        this.pos = new Vec3f(pos);
        this.tex = new Vec2f(0.0f, 0.0f);
        this.nrm = new Vec3f(0.0f, 0.0f, 1.0f);
        this.clr = new Vec3f(1.0f, 1.0f, 1.0f);
    }

    public Vertex(Vec3f pos, Vec2f tex, Vec3f nrm, Vec3f clr) {
        this.pos = new Vec3f(pos);
        this.tex = new Vec2f(tex);
        this.nrm = new Vec3f(nrm);
        this.clr = new Vec3f(clr);
    }

    @Override
    public Vertex clone() {
        return new Vertex(pos, tex, nrm, clr);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.pos);
        hash = 89 * hash + Objects.hashCode(this.tex);
        hash = 89 * hash + Objects.hashCode(this.nrm);
        hash = 89 * hash + Objects.hashCode(this.clr);
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
        if (!Objects.equals(this.pos, other.pos)) {
            return false;
        }
        if (!Objects.equals(this.tex, other.tex)) {
            return false;
        }
        if (!Objects.equals(this.nrm, other.nrm)) {
            return false;
        }
        if (!Objects.equals(this.clr, other.clr)) {
            return false;
        }
        return true;
    }

}
