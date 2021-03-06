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
package jmodelling.engine.object.camera;

import com.jogamp.opengl.GL2;
import jmodelling.engine.object.bounds.BoundingSphere;
import jmodelling.engine.raytracing.Ray;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class CamArcball extends Cam {

    public static final OrthoType ORTHO = new OrthoType();
    public static final PerspectiveType PERSPECTIVE = new PerspectiveType();

    public CamType type;
    public float zNear, zFar;
    public float distToTarget;
    public float fov;
    public float orthoScale;

    public CamArcball(String name, Vec3f loc, Vec3f rot, CamType type,
            float zNear, float zFar, float distToTarget, float fov) {
        super(name, loc, rot);
        this.type = type;
        this.zNear = zNear;
        this.zFar = zFar;
        this.distToTarget = distToTarget;
        this.fov = fov;
        this.orthoScale = distToTarget;
    }

    public CamArcball(String name, Vec3f loc, Vec3f tar, CamType type,
            float zNear, float zFar, float fov) {
        super(name, loc, dirToRot_(tar.sub_(loc)));
        this.type = type;
        this.zNear = zNear;
        this.zFar = zFar;
        this.distToTarget = tar.sub_(loc).norm();
        this.fov = fov;
        this.orthoScale = distToTarget;
    }

    public void orbit(Vec3f dRot) {
        //Get target position before rotation
        Vec3f tar = getTar();

        //Rotate angles
        rot.add(dRot);

        //Rotate camera position around target
        loc = tar.add(getDir().negate().scale(distToTarget));
    }

    public void rotateAroundTarget(Vec3f newRot) {
        //Get target position before rotation
        Vec3f tar = getTar();
        
        //Set rotation angles
        rot.set(newRot);
        
        //Rotate camera position around target
        loc = tar.add(getDir().negate().scale(distToTarget));
    }

    public void translate(Vec2f deltaView) {
        type.translate(this, deltaView);
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

    public void zoom(float delta) {
        type.zoom(this, delta);
    }

    public void toggleType() {
        if (type == ORTHO) {
            type = PERSPECTIVE;
            loc = getTar().add(getDir().negate().scale(orthoScale));
            distToTarget = orthoScale;
        } else {
            type = ORTHO;
            orthoScale = distToTarget;
        }
    }

    public void setType(CamType newType) {
        this.type = newType;
    }

    @Override
    public Ray viewPosToRay(Vec2f posView) {
        return type.viewPosToRay(this, posView);
    }

    @Override
    public Ray viewPosToRayAspect(Vec2f posView, float aspect) {
        return type.viewPosToRayAspect(this, posView, aspect);
    }

    @Override
    public Ray viewPosToRay(int xMouse, int yMouse, int screenWidth, int screenHeight) {
        return type.viewPosToRay(this, xMouse, yMouse, screenWidth, screenHeight);
    }

    @Override
    public Mat4f getProjectionMatrix(float aspect) {
        return type.getProjectionMatrix(this, aspect);
    }

    @Override
    public Mat4f getModelViewMatrix() {
        return type.getModelViewMatrix(this);
    }

    @Override
    public void renderOpaque(GL2 gl) {

    }

    @Override
    public void renderWireframe(GL2 gl) {

    }

    @Override
    public BoundingSphere getBoundingSphere() {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public String getType() {
        return "ARCBALL_CAM";
    }

}
