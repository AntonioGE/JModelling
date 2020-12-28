/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.math.mat;

import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Mat3f {

    public float m00, m01, m02, m10, m11, m12, m20, m21, m22;

    public Mat3f() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;

        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;

        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
    }

    public Mat3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        set(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    public Mat3f(Mat3f other) {
        set(other);
    }

    public Mat3f(float[] data) {
        set(data);
    }

    @Override
    public Mat3f clone() {
        return new Mat3f(this);
    }

    @Override
    public String toString() {
        return "|" + m00 + ", " + m01 + ", " + m02 + "|\n"
                + "|" + m10 + ", " + m11 + ", " + m12 + "|\n"
                + "|" + m20 + ", " + m21 + ", " + m22 + "|";
    }

    public void print() {
        System.out.println(toString());
    }
    
    public final void set(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;

        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;

        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    public final void set(Mat3f other) {
        this.m00 = other.m00;
        this.m01 = other.m01;
        this.m02 = other.m02;

        this.m10 = other.m10;
        this.m11 = other.m11;
        this.m12 = other.m12;

        this.m20 = other.m20;
        this.m21 = other.m21;
        this.m22 = other.m22;
    }

    public final void set(float[] data) {
        this.m00 = data[0];
        this.m01 = data[1];
        this.m02 = data[2];

        this.m10 = data[3];
        this.m11 = data[4];
        this.m12 = data[5];

        this.m20 = data[6];
        this.m21 = data[7];
        this.m22 = data[8];
    }

    public float[] toArray() {
        return new float[]{
            m00, m01, m02, m10, m11, m12, m20, m21, m22
        };
    }

    public static Mat3f identity() {
        return new Mat3f(
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f
        );
    }

    public static void add(Mat3f src1, Mat3f src2, Mat3f dst) {
        dst.m00 = src1.m00 + src2.m00;
        dst.m01 = src1.m01 + src2.m01;
        dst.m02 = src1.m02 + src2.m02;

        dst.m10 = src1.m10 + src2.m10;
        dst.m11 = src1.m11 + src2.m11;
        dst.m12 = src1.m12 + src2.m12;

        dst.m20 = src1.m20 + src2.m20;
        dst.m21 = src1.m21 + src2.m21;
        dst.m22 = src1.m22 + src2.m22;
    }

    public static Mat3f add_(Mat3f src1, Mat3f src2) {
        Mat3f dst = new Mat3f();
        add(src1, src2, dst);
        return dst;
    }

    public Mat3f add(Mat3f other) {
        add(this, other, this);
        return this;
    }

    public Mat3f add_(Mat3f other) {
        return add_(this, other);
    }

    public static void sub(Mat3f src1, Mat3f src2, Mat3f dst) {
        dst.m00 = src1.m00 - src2.m00;
        dst.m01 = src1.m01 - src2.m01;
        dst.m02 = src1.m02 - src2.m02;

        dst.m10 = src1.m10 - src2.m10;
        dst.m11 = src1.m11 - src2.m11;
        dst.m12 = src1.m12 - src2.m12;

        dst.m20 = src1.m20 - src2.m20;
        dst.m21 = src1.m21 - src2.m21;
        dst.m22 = src1.m22 - src2.m22;
    }

    public static Mat3f sub_(Mat3f src1, Mat3f src2) {
        Mat3f dst = new Mat3f();
        sub(src1, src2, dst);
        return dst;
    }

    public Mat3f sub(Mat3f other) {
        sub(this, other, this);
        return this;
    }

    public Mat3f sub_(Mat3f other) {
        return sub_(this, other);
    }

    public static void mul(Mat3f src1, Mat3f src2, Mat3f dst) {
        dst.m00 = src1.m00 * src2.m00 + src1.m01 * src2.m10 + src1.m02 * src2.m20;
        dst.m01 = src1.m00 * src2.m01 + src1.m01 * src2.m11 + src1.m02 * src2.m21;
        dst.m02 = src1.m00 * src2.m02 + src1.m01 * src2.m12 + src1.m02 * src2.m22;

        dst.m10 = src1.m10 * src2.m00 + src1.m11 * src2.m10 + src1.m12 * src2.m20;
        dst.m11 = src1.m10 * src2.m01 + src1.m11 * src2.m11 + src1.m12 * src2.m21;
        dst.m12 = src1.m10 * src2.m02 + src1.m11 * src2.m12 + src1.m12 * src2.m22;

        dst.m20 = src1.m20 * src2.m00 + src1.m21 * src2.m10 + src1.m22 * src2.m20;
        dst.m21 = src1.m20 * src2.m01 + src1.m21 * src2.m11 + src1.m22 * src2.m21;
        dst.m22 = src1.m20 * src2.m02 + src1.m21 * src2.m12 + src1.m22 * src2.m22;
    }

    public static Mat3f mul_(Mat3f src1, Mat3f src2) {
        Mat3f dst = new Mat3f();
        mul(src1, src2, dst);
        return dst;
    }

    public Mat3f mul(Mat3f other) {
        mul(this.clone(), other, this);
        return this;
    }

    public Mat3f mul_(Mat3f other) {
        return mul_(this, other);
    }

    public void setRow(int index, Vec3f row) {
        float[] data = toArray();
        index *= 3;
        data[index] = row.x;
        data[index + 1] = row.y;
        data[index + 2] = row.z;
        set(data);
    }

    public void setCol(int index, Vec3f col) {
        float[] data = toArray();
        data[index] = col.x;
        data[index + 3] = col.y;
        data[index + 6] = col.z;
        set(data);
    }

}
