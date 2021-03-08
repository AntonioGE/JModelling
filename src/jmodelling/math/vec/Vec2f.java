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
package jmodelling.math.vec;

import java.nio.FloatBuffer;

/**
 *
 * @author ANTONIO
 */
public class Vec2f {
    
    public float x, y;
    

    public Vec2f() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vec2f(float x, float y) {
        set(x, y);
    }

    public Vec2f(Vec2f other) {
        set(other);
    }

    public Vec2f(float[] data) {
        this.x = data[0];
        this.y = data[1];
    }
    
    public Vec2f(float[] data, int offset) {
        this.x = data[offset];
        this.y = data[offset + 1];
    }

    @Override
    public Vec2f clone() {
        return new Vec2f(this);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Float.floatToIntBits(this.x);
        hash = 73 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vec2f other = (Vec2f) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return "[" + x + ", " + y +"]";
    }

    public void print() {
        System.out.println(toString());
    }
    
    public void print(String name){
        System.out.println(name + ": " + toString());
    }

    public Vec2f set(Vec2f other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }
    
    public Vec2f set(float x, float y){
        this.x = x;
        this.y = y;
        return this;
    }

    public float[] toArray() {
        return new float[]{x, y};
    }
    
    public void writeInBuffer(FloatBuffer buffer, int offset) {
        buffer.put(offset, x);
        buffer.put(offset + 1, y);
    }

    /**
     * Writes the vector data into a array at the offset location
     *
     * @param array array to write in
     * @param offset the position
     * @param src the vector to write
     */
    public static void writeInArray(float[] array, int offset, Vec2f src) {
        array[offset] = src.x;
        array[offset + 1] = src.y;
    }

    /**
     * Writes the vector data into a array at the offset location
     *
     * @param array array to write in
     * @param offset the position
     */
    public void writeInArray(float[] array, int offset) {
        writeInArray(array, offset, this);
    }
    
    public float norm() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static void normalize(Vec2f src, Vec2f dst) {
        float invNorm = 1.0f / src.norm();
        dst.x = src.x * invNorm;
        dst.y = src.y * invNorm;
    }

    public static Vec2f normalize(Vec2f src) {
        normalize(src, src);
        return src;
    }

    public static Vec2f normalize_(Vec2f src) {
        Vec2f dst = new Vec2f();
        normalize(src, dst);
        return dst;
    }

    public Vec2f normalize() {
        return normalize(this);
    }

    public Vec2f normalize_() {
        return normalize_(this);
    }

    public static float dist(Vec2f src1, Vec2f src2) {
        return src1.sub_(src2).norm();
    }

    public float dist(Vec2f other) {
        return dist(this, other);
    }
    
    public static void add(Vec2f src1, Vec2f src2, Vec2f dst) {
        dst.x = src1.x + src2.x;
        dst.y = src1.y + src2.y;
    }

    public static Vec2f add_(Vec2f src1, Vec2f src2) {
        Vec2f dst = new Vec2f();
        add(src1, src2, dst);
        return dst;
    }

    public Vec2f add(Vec2f other) {
        add(this, other, this);
        return this;
    }

    public Vec2f add_(Vec2f other) {
        return add_(this, other);
    }
    
    public static void add(Vec2f src, float x, float y, Vec2f dst){
        dst.x = src.x + x;
        dst.y = src.y + y;
    }
    
    public static Vec2f add_(Vec2f src, float x, float y){
        Vec2f dst = new Vec2f();
        add(src, x, y, dst);
        return dst;
    }
    
    public Vec2f add(float x, float y){
        add(this, x, y, this);
        return this;
    }
    
    public Vec2f add_(float x, float y){
        return add_(this, x, y);
    }
    
    public static void sub(Vec2f src1, Vec2f src2, Vec2f dst) {
        dst.x = src1.x - src2.x;
        dst.y = src1.y - src2.y;
    }

    public static Vec2f sub_(Vec2f src1, Vec2f src2) {
        Vec2f dst = new Vec2f();
        sub(src1, src2, dst);
        return dst;
    }

    public Vec2f sub(Vec2f other) {
        sub(this, other, this);
        return this;
    }

    public Vec2f sub_(Vec2f other) {
        return sub_(this, other);
    }

    public static float dot(Vec2f src1, Vec2f src2) {
        return src1.x * src2.x + src1.y * src2.y;
    }

    public float dot(Vec2f other) {
        return dot(this, other);
    }

    
    //TODO: Create Mat2f class and implement multiplication with Vec2f
    /*
    public static void mul(Mat3f src1, Vec3f src2, Vec3f dst) {
        dst.x = src1.m00 * src2.x + src1.m01 * src2.y + src1.m02 * src2.z;
        dst.y = src1.m10 * src2.x + src1.m11 * src2.y + src1.m12 * src2.z;
        dst.z = src1.m20 * src2.x + src1.m21 * src2.y + src1.m22 * src2.z;
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
    */

    public static void scale(Vec2f src, float scale, Vec2f dst) {
        dst.x = src.x * scale;
        dst.y = src.y * scale;
    }

    public static Vec2f scale_(Vec2f src, float scale) {
        Vec2f dst = new Vec2f();
        scale(src, scale, dst);
        return dst;
    }

    public Vec2f scale(float scale) {
        scale(this, scale, this);
        return this;
    }

    public Vec2f scale_(float scale) {
        return scale_(this, scale);
    }

    public static void negate(Vec2f src, Vec2f dst) {
        dst.x = -src.x;
        dst.y = -src.y;
    }

    public static Vec2f negate_(Vec2f src) {
        Vec2f dst = new Vec2f();
        negate(src, dst);
        return dst;
    }

    public Vec2f negate() {
        negate(this, this);
        return this;
    }

    public Vec2f negate_() {
        return negate_(this);
    }

    public static float angle(Vec2f src){
        return (float) Math.atan2(src.y, src.x);
    }
    
    public float angle(){
        return angle(this);
    }
    
    public static float angleDeg(Vec2f src){
        return (float) Math.toDegrees(angle(src));
    }
    
    public float angleDeg(){
        return angleDeg(this);
    }
    
    /**
     * Proyects one vector onto another
     * 
     * @param src vector to be proyected
     * @param dir direction of the proyection
     * @param dst output proyected vector
     */
    public static void proy(Vec2f src, Vec2f dir, Vec2f dst){
        dst.set(dir).normalize();
        dst.scale(src.dot(dst));
    }
    
    /**
     * Proyects one vector onto another
     * 
     * @param src vector to be proyected
     * @param dir direction of the proyection
     * @return new proyected vector
     */
    public static Vec2f proy_(Vec2f src, Vec2f dir){
        Vec2f dst = new Vec2f();
        proy(src, dir, dst);
        return dst;
    }
    
    /**
     * Proyects this vector onto another
     * 
     * @param dir direction of the proyection
     * @return this proyected vector
     */
    public Vec2f proy(Vec2f dir){
        proy(this.clone(), dir, this);
        return this;
    }
    
    /**
     * Proyects this vector onto another
     * 
     * @param dir direction of the proyection
     * @return new proyected vector
     */
    public Vec2f proy_(Vec2f dir){
        return proy_(this, dir);
    }

}
