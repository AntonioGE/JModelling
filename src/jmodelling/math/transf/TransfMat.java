/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.math.transf;

import jmodelling.math.mat.Mat3f;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class TransfMat {

    public static void perspective(float fovy, float aspect, float zNear, float zFar, Mat4f dst) {
        float tanHalfFovy = (float) Math.tan(Math.toDegrees(fovy) * 0.5f);
        dst.m00 = 1.0f / (aspect * tanHalfFovy);
        dst.m01 = 0.0f;
        dst.m02 = 0.0f;
        dst.m03 = 0.0f;
        dst.m10 = 0.0f;
        dst.m11 = 1.0f / tanHalfFovy;
        dst.m12 = 0.0f;
        dst.m13 = 0.0f;
        dst.m20 = 0.0f;
        dst.m21 = 0.0f;
        dst.m22 = -(zFar + zNear) / (zFar - zNear);
        dst.m23 = -1.0f;
        dst.m30 = 0.0f;
        dst.m31 = 0.0f;
        dst.m32 = -2.0f * zFar * zNear / (zFar - zNear);
        dst.m33 = 0.0f;
    }

    public static Mat4f perspective_(float fovy, float aspect, float zNear, float zFar) {
        Mat4f dst = new Mat4f();
        perspective(fovy, aspect, zNear, zFar, dst);
        return dst;
    }
    
    public static void ortho(float left, float right, float bottom, float top, float zNear, float zFar, Mat4f dst) {
        dst.m00 = 2.0f / (right - left);
        dst.m01 = 0.0f;
        dst.m02 = 0.0f;
        dst.m03 = 0.0f;
        dst.m10 = 0.0f;
        dst.m11 = 2.0f / (top - bottom);
        dst.m12 = 0.0f;
        dst.m13 = 0.0f;
        dst.m20 = 0.0f;
        dst.m21 = 0.0f;
        dst.m22 = -2.0f / (zFar - zNear);
        dst.m23 = 0.0f;
        dst.m30 = -(right + left) / (right - left);
        dst.m31 = -(top + bottom) / (top - bottom);
        dst.m32 = -(zFar + zNear) / (zFar - zNear);
        dst.m33 = 1.0f;
    }
    
    public static Mat4f ortho_(float left, float right, float bottom, float top, float zNear, float zFar) {
        Mat4f dst = new Mat4f();
        ortho(left, right, bottom, top, zNear, zFar, dst);
        return dst;
    }

    public static void rotation3f(float degrees, Vec3f axis, Mat3f dst){
        final float cos = (float) Math.cos(Math.toRadians(degrees));
        final float sin = (float) Math.sin(Math.toRadians(degrees));
        dst.m00 = cos + axis.x * axis.x * (1.0f - cos);
        dst.m01 = axis.x * axis.y * (1.0f - cos) - axis.z * sin;
        dst.m02 = axis.x * axis.z * (1.0f - cos) + axis.y * sin;
        
        dst.m10 = axis.y * axis.x * (1.0f - cos) + axis.z * sin;
        dst.m11 = cos + axis.y * axis.y * (1.0f - cos);
        dst.m12 = axis.y * axis.z * (1.0f - cos) - axis.x * sin;
        
        dst.m20 = axis.z * axis.x * (1.0f - cos) - axis.y * sin;
        dst.m21 = axis.z * axis.y * (1.0f - cos) + axis.x * sin;
        dst.m22 = cos + axis.z * axis.z * (1.0f - cos);
    }
    
    public static Mat3f rotation3f_(float degrees, Vec3f axis){
        Mat3f dst = new Mat3f();
        rotation3f(degrees, axis, dst);
        return dst;
    }
    
    public static void rotation(float degrees, Vec3f axis, Mat4f dst){
        final float cos = (float) Math.cos(Math.toRadians(degrees));
        final float sin = (float) Math.sin(Math.toRadians(degrees));
        dst.m00 = cos + axis.x * axis.x * (1.0f - cos);
        dst.m01 = axis.x * axis.y * (1.0f - cos) - axis.z * sin;
        dst.m02 = axis.x * axis.z * (1.0f - cos) + axis.y * sin;
        dst.m03 = 0.0f;
        
        dst.m10 = axis.y * axis.x * (1.0f - cos) + axis.z * sin;
        dst.m11 = cos + axis.y * axis.y * (1.0f - cos);
        dst.m12 = axis.y * axis.z * (1.0f - cos) - axis.x * sin;
        dst.m13 = 0.0f;
        
        dst.m20 = axis.z * axis.x * (1.0f - cos) - axis.y * sin;
        dst.m21 = axis.z * axis.y * (1.0f - cos) + axis.x * sin;
        dst.m22 = cos + axis.z * axis.z * (1.0f - cos);
        dst.m23 = 0.0f;
        
        dst.m30 = 0.0f;
        dst.m31 = 0.0f;
        dst.m32 = 0.0f;
        dst.m33 = 1.0f;
    }
    
    public static Mat4f rotation_(float degrees, Vec3f axis){
        Mat4f dst = new Mat4f();
        rotation(degrees, axis, dst);
        return dst;
    }
    
    
    public static void translation(Vec3f trans, Mat4f dst){
        dst.m00 = 1.0f;
        dst.m01 = 0.0f;
        dst.m02 = 0.0f;
        dst.m03 = trans.x;
        
        dst.m10 = 0.0f;
        dst.m11 = 1.0f;
        dst.m12 = 0.0f;
        dst.m13 = trans.y;
        
        dst.m20 = 0.0f;
        dst.m21 = 0.0f;
        dst.m22 = 1.0f;
        dst.m23 = trans.z;
        
        dst.m30 = 0.0f;
        dst.m31 = 0.0f;
        dst.m32 = 0.0f;
        dst.m33 = 1.0f;
    }
    
    public static Mat4f translation_(Vec3f trans){
        Mat4f dst = new Mat4f();
        translation(trans, dst);
        return dst;
    }
    
}
