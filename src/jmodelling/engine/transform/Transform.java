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
package jmodelling.engine.transform;

import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;
import jmodelling.math.vec.Vec4f;
import jmodelling.math.mat.Mat4f;

/**
 *
 * @author ANTONIO
 */
public class Transform {

    /**
     * Converts a point from world coordinates to the view screen coordinates
     * 
     * @param src vector in world coordinates
     * @param camTransf camera transformation matrix
     * @param dst output vector in view coordinates
     */
    public static void worldToView(Vec3f src, Mat4f camTransf, Vec2f dst){
        Vec4f v = new Vec4f(src);
        v.mul(camTransf);
        v.scale(1.0f / v.w);
        dst.set(v.x, v.y);
    }
    
    /**
     * Converts a point from world coordinates to the view screen coordinates
     * 
     * @param src vector in world coordinates
     * @param camTransf camera transformation matrix
     * @return vector in view coordinates
     */
    public static Vec2f worldToView(Vec3f src, Mat4f camTransf ){
        Vec2f dst = new Vec2f();
        worldToView(src, camTransf, dst);
        return dst;
    }
    
    /**
     * Converts a vector from world coordinates to view screen coordinates
     * 
     * @param head coordinates of the vector's head
     * @param tail coordinates of the vector's tail
     * @param camTransf camera transformation matrix
     * @param dst output vector in view coordinates
     */
    public static void vectorWorldToView(Vec3f head, Vec3f tail, Mat4f camTransf, Vec2f dst){
        worldToView(head, camTransf, dst);
        dst.sub(worldToView(tail, camTransf));
    }
    
    /**
     * Converts a vector from world coordinates to view screen coordinates
     * 
     * @param head coordinates of the vector's head
     * @param tail coordinates of the vector's tail
     * @param camTransf camera transformation matrix
     * @return vector in view coordinates
     */
    public static Vec2f vectorWorldToView(Vec3f head, Vec3f tail, Mat4f camTransf){
        Vec2f dst = new Vec2f();
        vectorWorldToView(head, tail, camTransf, dst);
        return dst;
    }
    
    /*
    public static void vec3fToVec2f(){
        Vec4f p1 = new Vec4f(objPos.add_(moveAxis), 1.0f);
        Vec4f p2 = new Vec4f(objPos, 1.0f);
        p1.mul(camTransf);
        p2.mul(camTransf);
        p1.scale(1.0f / p1.w);
        p2.scale(1.0f / p2.w);
        Vec2f axis2D = new Vec2f(p1.x - p2.x, p1.y - p2.y);
        axis2D.x *= (float) screenWidth / screenHeight;
        axis2D.normalize();
    }
    
    public static void translateViewTo3D(
            Vec2f start, Vec2f end, 
            Vec3f objPos, Vec3f moveAxis, 
            Mat4f camTransf, 
            int screenWidth, int screenHeight) {
        
        Vec4f p1 = new Vec4f(objPos.add_(moveAxis), 1.0f);
        Vec4f p2 = new Vec4f(objPos, 1.0f);
        p1.mul(camTransf);
        p2.mul(camTransf);
        p1.scale(1.0f / p1.w);
        p2.scale(1.0f / p2.w);
        Vec2f axis2D = new Vec2f(p1.x - p2.x, p1.y - p2.y);
        axis2D.x *= (float) screenWidth / screenHeight;
        axis2D.normalize();

        Vec2f o2d = new Vec2f(p2.x * (float) screenWidth / screenHeight, p2.y);

        axis2D.print("axis 4D");

        Vec2f o = Cam.pixelToView(lastGrabX, lastGrabY, getWidth(), getHeight()); 
        Vec2f p = Cam.pixelToView(mouseX, mouseY, getWidth(), getHeight()); 

        float proyO = o.sub_(o2d).dot(axis2D);
        Vec2f q = o2d.add_(axis2D.scale_(proyO));

        float proyP = p.sub_(o2d).dot(axis2D);
        Vec2f r = o2d.add_(axis2D.scale_(proyP));

        Vec3f a = cam.viewPosToRay(q);
        Vec3f c = cam.viewPosToRay(r);
        Vec3f b = moveAxis;
        float d;
        d = (a.y * c.x - a.x * c.y) / (b.x * c.y - b.y * c.x) * distToObj;
        if (!Float.isFinite(d)) {
            d = (a.y * c.z - a.z * c.y) / (b.z * c.y - b.y * c.z) * distToObj;
            if (!Float.isFinite(d)) {
                d = (a.x * c.z - a.z * c.x) / (b.z * c.x - b.x * c.z) * distToObj;
            }
        }
    }
    */

}
