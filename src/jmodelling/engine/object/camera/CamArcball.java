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
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
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

    @Override
    public Vec3f viewPosToRay(Vec2f posView) {
        final float tan = (float) Math.tan(Math.toRadians(fov / 2.0f));
        return new Vec3f(posView.x * tan, posView.y * tan, -1.0f).normalize().mul(getLocalAxis3f());
    }

    @Override
    public Vec3f viewPosToRayAspect(Vec2f posView, float aspect) {
        Vec2f posViewAspect = new Vec2f(posView);
        posViewAspect.x *= aspect;
        return viewPosToRay(posViewAspect);
    }

    @Override
    public Vec3f viewPosToRay(int xMouse, int yMouse, int screenWidth, int screenHeight) {
        return viewPosToRay(pixelToViewAspect(xMouse, yMouse, screenWidth, screenHeight));
    }

    @Override
    public BoundingSphere getBoundingSphere() {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }
}
