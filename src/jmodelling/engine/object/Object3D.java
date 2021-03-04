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
package jmodelling.engine.object;

import com.jogamp.opengl.GL2;
import java.util.Objects;
import jmodelling.engine.object.bounds.BoundingSphere;
import jmodelling.engine.object.transform.Transform;
import jmodelling.engine.transform.Transformation;
import jmodelling.math.mat.Mat3f;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public abstract class Object3D {

    public String name;
    public Vec3f loc;
    public Vec3f rot;
    public Vec3f sca;

    public Object3D(String name, Vec3f loc, Vec3f rot, Vec3f sca) {
        this.name = name;
        this.loc = loc;
        this.rot = rot;
        this.sca = sca;
    }

    public Object3D(String name, Vec3f loc, Vec3f rot) {
        this(name, loc, rot, new Vec3f(1.0f, 1.0f, 1.0f));
    }

    public Object3D(String name, Vec3f loc) {
        this(name, loc, new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(1.0f, 1.0f, 1.0f));
    }

    public Object3D(Vec3f loc, Vec3f rot, Vec3f sca) {
        this("", loc, rot, sca);
    }

    public Object3D(Vec3f loc, Vec3f rot) {
        this("", loc, rot, new Vec3f(1.0f, 1.0f, 1.0f));
    }

    public Object3D() {
        this(new Vec3f(0.0f, 0.0f, 0.0f),
                new Vec3f(0.0f, 0.0f, 0.0f),
                new Vec3f(1.0f, 1.0f, 1.0f));
    }

    public Object3D(Vec3f loc) {
        this(loc, new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(1.0f, 1.0f, 1.0f));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.name);
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
        final Object3D other = (Object3D) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public abstract void renderOpaque(GL2 gl);

    public abstract void init(GL2 gl);

    public abstract void update(GL2 gl);

    public abstract void delete(GL2 gl);

    public abstract BoundingSphere getBoundingSphere();
    
    public abstract boolean isSelectable();
    
    public abstract String getType();
    
    public Mat4f getLocalAxis() {
        Mat4f t = TransfMat.translation_(loc);
        Mat4f rx = TransfMat.rotationDeg_(rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotationDeg_(rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotationDeg_(rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f s = TransfMat.scale_(sca);

        return t.mul(rz.mul(ry.mul(rx.mul(s))));
    }

    public Mat3f getLocalAxis3f() {
        return TransfMat.eulerDegToMat_(rot);
    }

    public Transform getTransform(){
        return new Transform(this);
    }
    
    
}
