/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling;

import jmodelling.math.mat.Mat3f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class MainFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Vec3f a = new Vec3f(2, 0, 1);
        Vec3f b = new Vec3f(1, -1, 3);
        Vec3f c = Vec3f.cross_(a, b);

        Mat3f A = new Mat3f(
                2, 3, 0,
                7, 1, 4,
                3, 0, -3
        );

        Mat3f B = new Mat3f(
                7, 0, 6,
                0, -2, 4,
                1, 0, 9
        );

        Mat3f C = new Mat3f();
        Mat3f.mul(A, B, C);
        
        C.print();
        
    }

}
