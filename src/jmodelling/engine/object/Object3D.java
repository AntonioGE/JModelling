/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object;

import com.jogamp.opengl.GL2;
import jmodelling.math.mat.Mat3f;
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
    
    public Mat3f getLocalAxis(){
        Mat3f rx = TransfMat.rotation3f_(rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat3f ry = TransfMat.rotation3f_(rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat3f rz = TransfMat.rotation3f_(rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        
        return rx.mul(ry.mul(rz));//TODO: Check multiplication order order
    }
}
