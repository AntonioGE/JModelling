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

    public Mat3f(Mat3f other) {
        set(other);
    }

    public Mat3f(float[] data) {
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

    @Override
    public Mat3f clone() {
        return new Mat3f(this);
    }

    @Override
    public String toString() {
        return "|" + m00 + ", " + m01 + ", " + m02 + "|"
                + "|" + m10 + ", " + m11 + ", " + m12 + "|"
                + "|" + m20 + ", " + m21 + ", " + m22 + "|";
    }
    
    public void print(){
        System.out.println(toString());
    }
    
    public void set(Mat3f other){
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

}
