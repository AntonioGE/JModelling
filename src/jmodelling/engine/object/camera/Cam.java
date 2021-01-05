/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.camera;

import jmodelling.engine.object.Object3D;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public abstract class Cam extends Object3D {

    public Cam(String name, Vec3f loc, Vec3f rot) {
        super(name, loc, rot);
    }

    public static void rotToDir(Vec3f rot, Vec3f dir) {
        dir.set(rot.add_(90.0f, 0.0f, -90.0f).anglesXZDegToVector());
        dir.z = -dir.z;
    }

    public static Vec3f rotToDir_(Vec3f angles) {
        Vec3f dir = new Vec3f();
        rotToDir(angles, dir);
        return dir;
    }

    public static void dirToRot(Vec3f dir, Vec3f rot) {
        rot.set(dir.anglesXZDeg_());
        rot.add(new Vec3f(90.0f, 0.0f, -90.0f));
    }

    public static Vec3f dirToRot_(Vec3f dir) {
        Vec3f rot = new Vec3f();
        dirToRot(dir, rot);
        return rot;
    }

    public Vec3f getDir() {
        return rotToDir_(rot);
    }

    public void setDir(Vec3f dir) {
        dirToRot(dir, rot);
    }

}
