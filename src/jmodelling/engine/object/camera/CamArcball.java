/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.camera;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class CamArcball extends Cam {

    public static enum Type {
        PERSPECTIVE, ORTHO

    }
    public Type type;
    public float zNear, zFar;
    public float distToTarget;
    public float fov;
    public float orthoScale;

    public CamArcball(String name, Vec3f loc, Vec3f rot, Type type,
            float zNear, float zFar, float distToTarget, float fov, float orthoScale) {
        super(name, loc, rot);
        this.type = type;
        this.zNear = zNear;
        this.zFar = zFar;
        this.distToTarget = distToTarget;
        this.fov = fov;
        this.orthoScale = orthoScale;
    }

    public CamArcball(String name, Vec3f loc, Vec3f tar, Type type,
            float zNear, float zFar, float fov, float orthoScale) {
        super(name, loc, dirToRot_(tar.sub_(loc)));
        this.type = type;
        this.zNear = zNear;
        this.zFar = zFar;
        this.distToTarget = tar.sub_(loc).norm();
        this.fov = fov;
        this.orthoScale = orthoScale;
    }

    @Override
    public void renderOpaque(GL2 gl) {

    }

    public void orbit(Vec3f dRot) {
        //Get target position before rotation
        Vec3f tar = getTar();

        //Rotate angles
        rot.add(dRot);

        //Rotate camera position around target
        loc = tar.add(getDir().negate().scale(distToTarget));
    }

    public static void locRotToTarget(Vec3f loc, Vec3f rot, float distToTarget, Vec3f tar) {
        Vec3f dir = Cam.rotToDir_(rot);
        tar.set(dir.scale(distToTarget).add(loc));
    }

    public static Vec3f locRotToTarget_(Vec3f loc, Vec3f rot, float distToTarget) {
        Vec3f tar = new Vec3f();
        locRotToTarget(loc, rot, distToTarget, tar);
        return tar;
    }

    public Vec3f getTar() {
        return locRotToTarget_(loc, rot, distToTarget);
    }

    public void moveTowardsTarget(float distToTarget) {
        //Get target position before rotation
        Vec3f tar = getTar();

        //Move camera location
        loc = tar.add(getDir().negate().scale(distToTarget));

        //Set new distance to target
        this.distToTarget = distToTarget;
    }

    
}
