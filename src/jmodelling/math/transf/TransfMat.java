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
        float tanHalfFovy = (float) Math.tan(Math.toRadians(fovy) * 0.5f);
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
        dst.m23 = -2.0f * zFar * zNear / (zFar - zNear);
        dst.m30 = 0.0f;
        dst.m31 = 0.0f;
        dst.m32 = -1.0f;
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
    
    public static void lookAt(Vec3f pos, Vec3f tar, Vec3f up, Mat4f dst){
        Vec3f camDirection = pos.sub_(tar).normalize();
        Vec3f camRight = up.cross_(camDirection).normalize();
        Vec3f camUp = camDirection.cross_(camRight);
        
        
        dst.m00 = camRight.x;
        dst.m01 = camRight.y;
        dst.m02 = camRight.z;
        dst.m03 = 0.0f;
        
        dst.m10 = camUp.x;
        dst.m11 = camUp.y;
        dst.m12 = camUp.z;
        dst.m13 = 0.0f;
        
        dst.m20 = camDirection.x;
        dst.m21 = camDirection.y;
        dst.m22 = camDirection.z;
        dst.m23 = 0.0f;
        
        dst.m30 = 0.0f;
        dst.m31 = 0.0f;
        dst.m32 = 0.0f;
        dst.m33 = 1.0f;
        
        Mat4f trans = Mat4f.identity();
        trans.m03 = -pos.x;
        trans.m13 = -pos.y;
        trans.m23 = -pos.z;
        
        dst.mul(trans);
        
    }
    
    public static Mat4f lookAt_(Vec3f pos, Vec3f tar, Vec3f up){
        Mat4f dst = new Mat4f();
        lookAt(pos, tar, up, dst);
        return dst;
    }
}
