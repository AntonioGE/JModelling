/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.camera;

import com.jogamp.opengl.GL2;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class CamPersp extends Cam{
    
    public CamPersp(String name, Vec3f loc, Vec3f rot) {
        super(name, loc, rot);
    }

    @Override
    public void renderOpaque(GL2 gl) {

    }
    
}
