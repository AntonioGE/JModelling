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
import jmodelling.engine.object.Object3D;
import jmodelling.engine.raytracing.Ray;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
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
        dir.mul(TransfMat.eulerDegToMat_(rot));

        //Alternate method:
        //dir.set(rot.add_(90.0f, 0.0f, -90.0f).anglesXZDegToVector());
        //dir.z = -dir.z;
    }

    public static Vec3f rotToDir_(Vec3f angles) {
        Vec3f dir = new Vec3f(0.0f, 0.0f, -1.0f);
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
    
    public static void rotToUp(Vec3f rot, Vec3f dst){
        dst.set(0.0f, 1.0f, 0.0f);
        dst.mul(TransfMat.eulerDegToMat_(rot));
    }
    
    public static Vec3f rotToUp_(Vec3f rot){
        Vec3f dst = new Vec3f();
        rotToUp(rot, dst);
        return dst;
    }
    
    public Vec3f getUp(){
        return rotToUp_(rot);
    }

    public Vec3f getDir() {
        return rotToDir_(rot);
    }

    public void setDir(Vec3f dir) {
        dirToRot(dir, rot);
    }
    
    /**
     * Converts pixel screen coordinates to view coordinates
     *
     * @param xMouse pixel X coordiante
     * @param yMouse pixel Y coordinate
     * @param screenWidth screen width in pixels
     * @param screenHeight screen height in pixels
     * @return XY coordinates in view coordinates
     */
    public static Vec2f pixelToView(int xMouse, int yMouse, int screenWidth, int screenHeight) {
        //final float aspect = (float) screenWidth / screenHeight;
        return new Vec2f(
                ((float) xMouse / (screenWidth / 2) - 1.0f),
                -((float) yMouse / (screenHeight / 2) - 1.0f));
        /*
        final float aspect = (float) screenWidth / screenHeight;
        return new Vec2f(
                ((float) xMouse / (screenWidth / 2) - 1.0f) * aspect,
                -((float) yMouse / (screenHeight / 2) - 1.0f));*/
    }
    
    /**
     * Converts pixel screen coordinates to view coordinates applying the aspect ratio correction
     *
     * @param xMouse pixel X coordiante
     * @param yMouse pixel Y coordinate
     * @param screenWidth screen width in pixels
     * @param screenHeight screen height in pixels
     * @return XY coordinates in view coordinates
     */
    public static Vec2f pixelToViewAspect(int xMouse, int yMouse, int screenWidth, int screenHeight) {
        final float aspect = (float) screenWidth / screenHeight;
        return new Vec2f(
                ((float) xMouse / (screenWidth / 2) - 1.0f) * aspect,
                -((float) yMouse / (screenHeight / 2) - 1.0f));
    }

    public abstract Ray viewPosToRay(Vec2f posView);
    
    /**
     * Converts a point in 2D view coordinates into a ray applying the aspect ratio
     * 
     * @param posView point in 2D view coordinates 
     * @param aspect aspect ratio of the screen
     * @return ray
     */
    public abstract Ray viewPosToRayAspect(Vec2f posView, float aspect);
    
    public abstract Ray viewPosToRay(int xMouse, int yMouse, int screenWidth, int screenHeight);
    
    public abstract Mat4f getProjectionMatrix(float aspect);
    
    public Mat4f getModelViewMatrix(){
        Mat4f rx = TransfMat.rotationDeg_(-rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotationDeg_(-rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotationDeg_(-rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f t = TransfMat.translation_(loc.negate_());
        return rx.mul_(ry).mul(rz).mul(t);
    }
    
    @Override
    public abstract void renderOpaque(GL2 gl);
    
    @Override
    public void init(GL2 gl){
    
    };
    
    @Override
    public void update(GL2 gl){
    
    };
    
    @Override
    public void delete(GL2 gl){
    
    };
}
