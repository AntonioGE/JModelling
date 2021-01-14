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
    
    public Object3D(Vec3f loc, Vec3f rot){
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
    
    public abstract void renderOpaque(GL2 gl);
    
    public Mat4f getLocalAxis(){
        Mat4f t = TransfMat.translation_(loc);
        Mat4f rx = TransfMat.rotation_(rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotation_(rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotation_(rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        
        
        //return rz.mul(ry.mul(rx.mul(t)));
        return t.mul(rz.mul(ry.mul(rx)));
    }
    
    public Mat3f getLocalAxis3f(){
        Mat3f rx = TransfMat.rotation3f_(rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat3f ry = TransfMat.rotation3f_(rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat3f rz = TransfMat.rotation3f_(rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        
        return rz.mul(ry.mul(rx));
    }
    
}
