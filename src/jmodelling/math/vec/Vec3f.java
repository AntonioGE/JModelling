/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.math.vec;

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
    public Vec3f clone(){
        return new Vec3f(this);
    }
    
    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
    
    public void print(){
        System.out.println(toString());
    }
    
    public void set(Vec3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
    
    public float[] toFloatArray() {
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
    
    public static Vec3f normalize(Vec3f src){
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
    
    public Vec3f normalize_(){
        return normalize_(this);
    }
    
    public static void add(Vec3f src1, Vec3f src2, Vec3f dst){
        dst.x = src1.x + src2.x;
        dst.y = src1.y + src2.y;
        dst.z = src1.z + src2.z;
    }
    
    public static Vec3f add_(Vec3f src1, Vec3f src2){
        Vec3f dst = new Vec3f();
        add(src1, src2, dst);
        return dst;
    }

    public Vec3f add(Vec3f other){
        add(this, other, this);
        return this;
    }
    
    public Vec3f add_(Vec3f other){
        return add_(this, other);
    }
    
    public static void sub(Vec3f src1, Vec3f src2, Vec3f dst){
        dst.x = src1.x - src2.x;
        dst.y = src1.y - src2.y;
        dst.z = src1.z - src2.z;
    }
    
    public static Vec3f sub_(Vec3f src1, Vec3f src2){
        Vec3f dst = new Vec3f();
        sub(src1, src2, dst);
        return dst;
    }

    public Vec3f sub(Vec3f other){
        sub(this, other, this);
        return this;
    }
    
    public Vec3f sub_(Vec3f other){
        return sub_(this, other);
    }
    
    public static void dot(Vec3f src1, Vec3f src2, Vec3f dst){
        dst.x = src1.x * src2.x;
        dst.y = src1.y * src2.y;
        dst.z = src1.z * src2.z;
    }
    
    public static Vec3f dot_(Vec3f src1, Vec3f src2){
        Vec3f dst = new Vec3f();
        dot(src1, src2, dst);
        return dst;
    }
    
    public Vec3f dot(Vec3f other){
        dot(this, other, this);
        return this;
    }
    
    public Vec3f dot_(Vec3f other){
        return dot_(this, other);
    }
    
    public static void cross(Vec3f src1, Vec3f src2, Vec3f dst){
        dst.x = src1.y * src2.z - src1.z * src2.y;
        dst.y = src1.z * src2.x - src1.x * src2.z;
        dst.z = src1.x * src2.y - src1.y * src2.x;
    }
    
    public static Vec3f cross_(Vec3f src1, Vec3f src2){
        Vec3f dst = new Vec3f();
        cross(src1, src2, dst);
        return dst;
    }
    
    public Vec3f cross(Vec3f other){
        cross(this.clone(), other, this);
        return this;
    } 
    
    public Vec3f cross_(Vec3f other){
        return cross_(this, other);
    }
    
}
