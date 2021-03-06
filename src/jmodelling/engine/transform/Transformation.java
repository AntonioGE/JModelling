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

import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.raytracing.Ray;
import jmodelling.math.mat.Mat3f;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;
import jmodelling.math.vec.Vec4f;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;

/**
 *
 * @author ANTONIO
 */
public class Transformation {

    /**
     * Converts a point from world coordinates to the view screen coordinates
     *
     * @param src vector in world coordinates
     * @param camTransf camera transformation matrix
     * @param dst output vector in view coordinates
     */
    public static void worldToView(Vec3f src, Mat4f camTransf, Vec2f dst) {
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
    public static Vec2f worldToView_(Vec3f src, Mat4f camTransf) {
        Vec2f dst = new Vec2f();
        worldToView(src, camTransf, dst);
        return dst;
    }

    public static void worldToViewAspect(Vec3f src, Mat4f camTransf,
            float aspect, Vec2f dst) {
        worldToView(src, camTransf, dst);
        dst.x *= aspect;
    }

    public static Vec2f worldToViewAspect_(Vec3f src, Mat4f camTransf,
            float aspect) {
        Vec2f dst = new Vec2f();
        worldToViewAspect(src, camTransf, aspect, dst);
        return dst;
    }

    /**
     * Converts a vector from world coordinates to view screen coordinates
     *
     * @param head world coordinates of the vector's head
     * @param tail world coordinates of the vector's tail
     * @param camTransf camera transformation matrix
     * @param dst output vector in view coordinates
     */
    public static void vectorWorldToView(Vec3f head, Vec3f tail, Mat4f camTransf, Vec2f dst) {
        worldToView(head, camTransf, dst);
        dst.sub(worldToView_(tail, camTransf));
    }

    /**
     * Converts a vector from world coordinates to view screen coordinates
     *
     * @param head world coordinates of the vector's head
     * @param tail world coordinates of the vector's tail
     * @param camTransf camera transformation matrix
     * @return vector in view coordinates
     */
    public static Vec2f vectorWorldToView_(Vec3f head, Vec3f tail, Mat4f camTransf) {
        Vec2f dst = new Vec2f();
        vectorWorldToView(head, tail, camTransf, dst);
        return dst;
    }

    /**
     * Converts a translation along a 3D vector from view coordinates to world
     * coordinates
     *
     * @param src initial position of the point to be translated
     * @param dir direction of the translation
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param dst output Vec3f representing the translation
     */
    @Deprecated
    public static void linearTranslationOld(Vec3f src, Vec3f dir,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect,
            Vec3f dst) {

        //Convert the translation direction from world to view coordinates
        Vec2f dir2dTail = worldToView_(src, camTransf);
        Vec2f dir2dHead = worldToView_(src.add_(dir).scale(1.0f), camTransf);//TODO: Study how set the 1.0f value
        Vec2f dir2d = dir2dHead.sub_(dir2dTail).normalize();

        //Project the view points onto the translation line in view coordinates
        Vec2f p0Proy = p0.sub_(dir2dTail).proy(dir2d).add(dir2dTail);
        Vec2f p1Proy = p1.sub_(dir2dTail).proy(dir2d).add(dir2dTail);

        //Apply the aspect ratio to the projected points
        p0Proy.x *= aspect;
        p1Proy.x *= aspect;

        //Generate rays from view coordinates to world coordinates
        Vec3f a = cam.viewPosToRay(p0Proy).dir;
        Vec3f c = cam.viewPosToRay(p1Proy).dir;
        Vec3f b = dir;

        //Calculate the translation value
        float d;
        d = (a.y * c.x - a.x * c.y) / (b.x * c.y - b.y * c.x);
        if (!Float.isFinite(d)) {
            d = (a.y * c.z - a.z * c.y) / (b.z * c.y - b.y * c.z);
            if (!Float.isFinite(d)) {
                d = (a.x * c.z - a.z * c.x) / (b.z * c.x - b.x * c.z);
            }
        }

        //Calculate the distance between the initial point and the camera
        final float distToSrc = Vec3f.dist(src, cam.loc);

        dst.set(dir).scale(d * distToSrc);
    }

    /**
     * Converts a translation along a 3D vector from view coordinates to world
     * coordinates
     *
     * @param src initial position of the point to be translated
     * @param dir direction of the translation
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @return Vec3f representing the translation
     */
    @Deprecated
    public static Vec3f linearTranslationOld_(Vec3f src, Vec3f dir,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect) {
        Vec3f dst = new Vec3f();
        linearTranslationOld(src, dir, p0, p1, camTransf, cam, aspect, dst);
        return dst;
    }

    /**
     * Converts a translation along a 3D vector from view coordinates to world
     * coordinates
     *
     * @param src initial position of the point to be translated
     * @param dir direction of the translation
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param dst output Vec3f representing the translation
     */
    public static void linearTranslation(Vec3f src, Vec3f dir,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect,
            Vec3f dst) {

        //Convert the translation direction from world to view coordinates
        Vec2f dir2dTail = worldToView_(src, camTransf);
        Vec2f dir2dHead = worldToView_(src.add_(dir).scale(1.0f), camTransf);//TODO: Study how set the 1.0f value
        Vec2f dir2d = dir2dHead.sub_(dir2dTail).normalize();

        //Project the view points onto the translation line in view coordinates
        Vec2f p0Proy = p0.sub_(dir2dTail).proy(dir2d).add(dir2dTail);
        Vec2f p1Proy = p1.sub_(dir2dTail).proy(dir2d).add(dir2dTail);

        //Apply the aspect ratio to the projected points
        p0Proy.x *= aspect;
        p1Proy.x *= aspect;

        //Generate rays from view coordinates to world coordinates
        Ray aRay = cam.viewPosToRay(p0Proy);
        Ray cRay = cam.viewPosToRay(p1Proy);
        Vec3f distFocus = aRay.loc.sub_(cRay.loc);

        Vec3f a = aRay.dir;
        Vec3f c = cRay.dir;
        Vec3f b = dir;
        Vec3f d = distFocus.normalize_();

        float A = Vec3f.dist(src, aRay.loc);
        float B;
        float D = distFocus.norm();

        //Calculate the translation value
        if (d.isFinite()) {//4 sided polygon
            B = ((a.y * c.x - a.x * c.y) * A + (d.y * c.x - d.x * c.y) * D) / (b.x * c.y - b.y * c.x);
            if (!Float.isFinite(B)) {
                B = ((a.y * c.z - a.z * c.y) * A + (d.y * c.z - d.z * c.y) * D) / (b.z * c.y - b.y * c.z);
                if (!Float.isFinite(B)) {
                    B = ((a.x * c.z - a.z * c.x) * A + (d.x * c.z - d.z * c.x) * D) / (b.z * c.x - b.x * c.z);
                }
            }
        } else {//3 sided polygon
            B = (a.y * c.x - a.x * c.y) / (b.x * c.y - b.y * c.x) * A;
            if (!Float.isFinite(B)) {
                B = (a.y * c.z - a.z * c.y) / (b.z * c.y - b.y * c.z) * A;
                if (!Float.isFinite(B)) {
                    B = (a.x * c.z - a.z * c.x) / (b.z * c.x - b.x * c.z) * A;
                }
            }
        }

        dst.set(dir).scale(B);
    }

    /**
     * Converts a translation along a 3D vector from view coordinates to world
     * coordinates
     *
     * @param src initial position of the point to be translated
     * @param dir direction of the translation
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @return Vec3f representing the translation
     */
    public static Vec3f linearTranslation_(Vec3f src, Vec3f dir,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect) {
        Vec3f dst = new Vec3f();
        linearTranslation(src, dir, p0, p1, camTransf, cam, aspect, dst);
        return dst;
    }

    /**
     * Converts a translation from view coordinates to a translation in a plane
     * perpendicular to the camera direction in world coordinates
     *
     * @param src initial position of the point to be translated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param dst output vector representing the translation
     */
    @Deprecated
    public static void planarTranslationOld(Vec3f src,
            Vec2f p0, Vec2f p1,
            Cam cam, float aspect,
            Vec3f dst) {

        //Get camera direction
        Vec3f dir = cam.getDir();

        //Calculate the distance from the camera to the plane of translation
        float dist = src.projPointOnLine_(cam.loc, dir).sub(cam.loc).norm();

        //Generate rays for the initial point and final point
        Vec3f ray0 = cam.viewPosToRayAspect(p0, aspect).dir;
        Vec3f ray1 = cam.viewPosToRayAspect(p1, aspect).dir;

        //Calculate the translations for both points
        Vec3f t0 = ray0.scale_(1.0f / dir.dot(ray0));
        Vec3f t1 = ray1.scale_(1.0f / dir.dot(ray1));

        //Calculate the translation as the difference between the points
        dst.set(t1.sub_(t0).scale(dist));

        //Alternate method
        //Vec3f t0 = ray0.scale_(1.0f / dir.dot(ray0)).sub(dir).scale(dist);
        //Vec3f t1 = ray1.scale_(1.0f / dir.dot(ray1)).sub(dir).scale(dist);
        //dst.set(t1.sub_(t0));
    }

    
    /**
     * Converts a translation from view coordinates to a translation in a plane
     * perpendicular to the camera direction in world coordinates
     *
     * @param src initial position of the point to be translated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param cam camera
     * @param aspect screen aspect ratio
     * @return new vector representing the translation
     */
    @Deprecated
    public static Vec3f planarTranslationOld_(Vec3f src,
            Vec2f p0, Vec2f p1,
            Cam cam, float aspect) {
        Vec3f dst = new Vec3f();
        planarTranslationOld(src, p0, p1, cam, aspect, dst);
        return dst;
    }

    /**
     * Converts a translation from view coordinates to a translation in a plane
     * perpendicular to the camera direction in world coordinates
     *
     * @param src initial position of the point to be translated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param dst output vector representing the translation
     */
    public static void planarTranslation(Vec3f src,
            Vec2f p0, Vec2f p1,
            Cam cam, float aspect,
            Vec3f dst) {

        //Get camera direction
        Vec3f dir = cam.getDir();

        //Calculate the distance from the camera to the plane of translation
        float dist = src.projPointOnLine_(cam.loc, dir).sub(cam.loc).norm();

        //Generate rays for the initial point and final point
        Ray ray0 = cam.viewPosToRayAspect(p0, aspect);
        Ray ray1 = cam.viewPosToRayAspect(p1, aspect);

        //Calculate the translations for both points
        Vec3f t0 = ray0.dir.scale_(1.0f / dir.dot(ray0.dir));
        Vec3f t1 = ray1.dir.scale_(1.0f / dir.dot(ray1.dir));

        //Calculate the translation as the difference between the points
        dst.set(t1.sub_(t0).scale(dist).add(ray1.loc.sub(ray0.loc)));
    }
    
    /**
     * Converts a translation from view coordinates to a translation in a plane
     * perpendicular to the camera direction in world coordinates
     *
     * @param src initial position of the point to be translated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param cam camera
     * @param aspect screen aspect ratio
     * @return new vector representing the translation
     */
    public static Vec3f planarTranslation_(Vec3f src,
            Vec2f p0, Vec2f p1,
            Cam cam, float aspect) {
        Vec3f dst = new Vec3f();
        planarTranslation(src, p0, p1, cam, aspect, dst);
        return dst;
    }
    
    /**
     * Converts a rotation from view coordinates to a rotation around some axis
     * in world coordinates
     *
     * @param src initial position of the point to be rotated
     * @param dir axis of rotation
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param dst output matrix representing the rotation
     */
    public static void axisRotation(Vec3f src, Vec3f dir,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect,
            Mat3f dst) {

        Vec2f center = worldToView_(src, camTransf);
        Vec2f p0A = p0.clone();
        Vec2f p1A = p1.clone();

        center.x *= aspect;
        p0A.x *= aspect;
        p1A.x *= aspect;

        float angle = p0A.sub(center).angle() - p1A.sub(center).angle();

        //Negate the angle if the camera direction vector and rotation axis are
        //pointing on opposite directions
        if (cam.getDir().dot(dir) < 0.0f) {
            angle = -angle;
        }

        TransfMat.rotation3f(angle, dir, dst);
    }

    /**
     * Converts a rotation from view coordinates to a rotation around some axis
     * in world coordinates
     *
     * @param src initial position of the point to be rotated
     * @param dir axis of rotation
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @return new matrix representing the rotation
     */
    public static Mat3f axisRotation_(Vec3f src, Vec3f dir,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect) {
        Mat3f dst = new Mat3f();
        axisRotation(src, dir, p0, p1, camTransf, cam, aspect, dst);
        return dst;
    }

    /**
     * Converts a rotation from view coordinates to a rotation around a plane
     * perpendicular to the camera direction in world coordinates
     *
     * @param src initial position of the point to be rotated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param dst output matrix representing the rotation
     */
    public static void planarRotation(Vec3f src,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect,
            Mat3f dst) {

        axisRotation(src, cam.getDir(), p0, p1, camTransf, cam, aspect, dst);
    }

    /**
     * Converts a rotation from view coordinates to a rotation around a plane
     * perpendicular to the camera direction in world coordinates
     *
     * @param src initial position of the point to be rotated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @return new matrix representing the rotation
     */
    public static Mat3f planarRotation_(Vec3f src,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect) {
        Mat3f dst = new Mat3f();
        planarRotation(src, p0, p1, camTransf, cam, aspect, dst);
        return dst;
    }

    /**
     * Converts a rotation from view coordinates to a rotation around a ball.
     *
     * @param src initial position of the point to be rotated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param sensitivity amount of rotation per mouse moved
     * @param dst output matrix representing the rotation
     */
    public static void ballRotation(Vec3f src,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect,
            float sensitivity,
            Mat3f dst) {

        Vec2f mouse = p1.sub_(p0);

        Vec3f camUp = cam.getUp();
        Vec3f camDir = cam.getDir();

        camUp.rotate(camDir, -mouse.angle());

        TransfMat.rotation3f(mouse.norm() * sensitivity, camUp, dst);
    }

    /**
     * Converts a rotation from view coordinates to a rotation around a ball and
     * stores the result in a new matrix.
     *
     * @param src initial position of the point to be rotated
     * @param p0 initial position of point in view coordinates
     * @param p1 final position of point in view coordinates
     * @param camTransf camera transformation matrix
     * @param cam camera
     * @param aspect screen aspect ratio
     * @param sensitivity amount of rotation per mouse moved
     * @return matrix representing the rotation
     */
    public static Mat3f ballRotation_(Vec3f src,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, Cam cam, float aspect,
            float sensitivity) {

        Mat3f dst = new Mat3f();
        ballRotation(src, p0, p1, camTransf, cam, aspect, sensitivity, dst);
        return dst;
    }

    public static void scale(Vec3f src, boolean x, boolean y, boolean z,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, float aspect,
            Vec3f dst) {

        final Vec2f center = worldToView_(src, camTransf);
        final Vec2f p0A = p0.clone();
        final Vec2f p1A = p1.clone();

        center.x *= aspect;
        p0A.x *= aspect;
        p1A.x *= aspect;

        final float d0 = p0A.dist(center);
        final float d1 = p1A.dist(center);

        final float scale = d1 / d0;

        float xScale = 1.0f, yScale = 1.0f, zScale = 1.0f;
        if (x) {
            xScale = scale;
        }
        if (y) {
            yScale = scale;
        }
        if (z) {
            zScale = scale;
        }

        dst.set(xScale, yScale, zScale);
    }

    public static Vec3f scale_(Vec3f src, boolean x, boolean y, boolean z,
            Vec2f p0, Vec2f p1,
            Mat4f camTransf, float aspect) {
        Vec3f dst = new Vec3f();
        scale(src, x, y, z, p0, p1, camTransf, aspect, dst);
        return dst;
    }
}
