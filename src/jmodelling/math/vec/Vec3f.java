/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.math.vec;

import jmodelling.math.mat.Mat3f;

/**
 *
 * @author ANTONIO
 */
public class Vec3f {

    public float x, y, z;

    public Vec3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(Vec3f other) {
        set(other);
    }

    public Vec3f(float[] data) {
        this.x = data[0];
        this.y = data[1];
        this.z = data[2];
    }

    @Override
    public Vec3f clone() {
        return new Vec3f(this);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public void print() {
        System.out.println(toString());
    }

    public void set(Vec3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public float[] toArray() {
        return new float[]{x, y, z};
    }

    public float norm() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static void normalize(Vec3f src, Vec3f dst) {
        float invNorm = 1.0f / src.norm();
        dst.x = src.x * invNorm;
        dst.y = src.y * invNorm;
        dst.z = src.z * invNorm;
    }

    public static Vec3f normalize(Vec3f src) {
        normalize(src, src);
        return src;
    }

    public static Vec3f normalize_(Vec3f src) {
        Vec3f dst = new Vec3f();
        normalize(src, dst);
        return dst;
    }

    public Vec3f normalize() {
        return normalize(this);
    }

    public Vec3f normalize_() {
        return normalize_(this);
    }

    public static void add(Vec3f src1, Vec3f src2, Vec3f dst) {
        dst.x = src1.x + src2.x;
        dst.y = src1.y + src2.y;
        dst.z = src1.z + src2.z;
    }

    public static Vec3f add_(Vec3f src1, Vec3f src2) {
        Vec3f dst = new Vec3f();
        add(src1, src2, dst);
        return dst;
    }

    public Vec3f add(Vec3f other) {
        add(this, other, this);
        return this;
    }

    public Vec3f add_(Vec3f other) {
        return add_(this, other);
    }

    public static void sub(Vec3f src1, Vec3f src2, Vec3f dst) {
        dst.x = src1.x - src2.x;
        dst.y = src1.y - src2.y;
        dst.z = src1.z - src2.z;
    }

    public static Vec3f sub_(Vec3f src1, Vec3f src2) {
        Vec3f dst = new Vec3f();
        sub(src1, src2, dst);
        return dst;
    }

    public Vec3f sub(Vec3f other) {
        sub(this, other, this);
        return this;
    }

    public Vec3f sub_(Vec3f other) {
        return sub_(this, other);
    }

    public static void dot(Vec3f src1, Vec3f src2, Vec3f dst) {
        dst.x = src1.x * src2.x;
        dst.y = src1.y * src2.y;
        dst.z = src1.z * src2.z;
    }

    public static Vec3f dot_(Vec3f src1, Vec3f src2) {
        Vec3f dst = new Vec3f();
        dot(src1, src2, dst);
        return dst;
    }

    public Vec3f dot(Vec3f other) {
        dot(this, other, this);
        return this;
    }

    public Vec3f dot_(Vec3f other) {
        return dot_(this, other);
    }

    public static void cross(Vec3f src1, Vec3f src2, Vec3f dst) {
        dst.x = src1.y * src2.z - src1.z * src2.y;
        dst.y = src1.z * src2.x - src1.x * src2.z;
        dst.z = src1.x * src2.y - src1.y * src2.x;
    }

    public static Vec3f cross_(Vec3f src1, Vec3f src2) {
        Vec3f dst = new Vec3f();
        cross(src1, src2, dst);
        return dst;
    }

    public Vec3f cross(Vec3f other) {
        cross(this.clone(), other, this);
        return this;
    }

    public Vec3f cross_(Vec3f other) {
        return cross_(this, other);
    }

    public static void mul(Mat3f src1, Vec3f src2, Vec3f dst) {
        dst.x = src1.m00 * src2.x + src1.m01 * src2.y + src1.m02 * src2.z;
        dst.x = src1.m10 * src2.x + src1.m11 * src2.y + src1.m12 * src2.z;
        dst.x = src1.m20 * src2.x + src1.m21 * src2.y + src1.m22 * src2.z;
    }

    public static Vec3f mul_(Mat3f src1, Vec3f src2) {
        Vec3f dst = new Vec3f();
        mul(src1, src2, dst);
        return dst;
    }

    public Vec3f mul(Mat3f src) {
        mul(src, this.clone(), this);
        return this;
    }

    public Vec3f mul_(Mat3f src) {
        return mul_(src, this);
    }

    public static void rotate(Vec3f src, Vec3f axis, float degrees, Vec3f dst) {
        float cos = (float) Math.cos(Math.toRadians(degrees));
        float sin = (float) Math.sin(Math.toRadians(degrees));
        dst.x = axis.x * (axis.x * src.x + axis.y * src.y + axis.z * src.z) * (1.0f - cos)
                + src.x * cos
                + (-axis.z * src.y + axis.y * src.z) * sin;
        dst.y = axis.y * (axis.x * src.x + axis.y * src.y + axis.z * src.z) * (1.0f - cos)
                + src.y * cos
                + (axis.z * src.x - axis.x * src.z) * sin;
        dst.z = axis.z * (axis.x * src.x + axis.y * src.y + axis.z * src.z) * (1.0f - cos)
                + src.z * cos
                + (-axis.y * src.x + axis.x * src.y) * sin;
    }

    public static Vec3f rotate_(Vec3f src, Vec3f axis, float degrees) {
        Vec3f dst = new Vec3f();
        rotate(src, axis, degrees, dst);
        return dst;
    }

    public Vec3f rotate(Vec3f axis, float degrees) {
        rotate(this.clone(), axis, degrees, this);
        return this;
    }

    public Vec3f rotate_(Vec3f axis, float degrees) {
        return rotate_(this, axis, degrees);
    }
    
    public static void rotateAround(Vec3f src, Vec3f center, Vec3f axis, float degrees, Vec3f dst){
        Vec3f srcMoved = src.sub_(center);
        rotate(srcMoved, axis, degrees, dst);
        dst.add(center);
    }
    
    public static Vec3f rotateAround_(Vec3f src, Vec3f center, Vec3f axis, float degrees){
        Vec3f dst = new Vec3f();
        rotateAround(src, center, axis, degrees, dst);
        return dst;
    }
    
    public Vec3f rotateAround(Vec3f center, Vec3f axis, float degrees){
        rotateAround(this.clone(), center, axis, degrees, this);
        return this;
    }
    
    public Vec3f rotateAround_(Vec3f center, Vec3f axis, float degrees){
        return rotateAround_(this, center, axis, degrees);
    }
    
    public static void negate(Vec3f src, Vec3f dst){
        dst.x = -src.x;
        dst.y = -src.y;
        dst.z = -src.z;
    }
    
    public static Vec3f negate_(Vec3f src){
        Vec3f dst = new Vec3f();
        negate(src, dst);
        return dst;
    }
    
    public Vec3f negate(){
        negate(this, this);
        return this;
    }
    
    public Vec3f negate_(){
        return negate_(this);
    }
}
