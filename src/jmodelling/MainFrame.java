/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling;

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
        
    }
    
}
