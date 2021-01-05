/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.camera;

import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class CamArcball extends Cam {

    public float distToTarget;

    public CamArcball(String name, Vec3f loc, Vec3f rot, float distToTarget) {
        super(name, loc, rot);
        this.distToTarget = distToTarget;
    }
   
    public CamArcball(String name, Vec3f loc, Vec3f tar){
        super(name, loc, dirToRot_(tar.sub_(loc)));
        distToTarget = tar.sub_(loc).norm();
    }
    
    public void orbit(Vec3f dRot){
        //Get target position before rotation
        Vec3f tar = getTar();
        
        //Rotate angles
        rot.add(dRot);
        
        //Rotate camera position around target
        loc = tar.add(getDir().negate().scale(distToTarget));
    }
    
    public static void locRotToTarget(Vec3f loc, Vec3f rot, float distToTarget, Vec3f tar){
        Vec3f dir = Cam.rotToDir_(rot);
        tar.set(dir.scale(distToTarget).add(loc));
    }
    
    public static Vec3f locRotToTarget_(Vec3f loc, Vec3f rot, float distToTarget){
        Vec3f tar = new Vec3f();
        locRotToTarget(loc, rot, distToTarget, tar);
        return tar;
    }
    
    public Vec3f getTar(){
        //return getDir().scale(distToTarget).add(loc);
        return locRotToTarget_(loc, rot, distToTarget);
    }
    
    public void moveTowardsTarget(float distToTarget){
        //Get target position before rotation
        Vec3f tar = getTar();
        
        //Move camera location
        loc = tar.add(getDir().negate().scale(distToTarget));
        
        //Set new distance to target
        this.distToTarget = distToTarget;
    }
    
}
