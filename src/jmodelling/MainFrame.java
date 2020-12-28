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

        Vec3f a = new Vec3f(1, 0, 0);
        Vec3f axis = new Vec3f(0, 0, 1);
        a.rotate(axis, 45).print();
        a.rotate(axis, 180).print();
        
    }

}
