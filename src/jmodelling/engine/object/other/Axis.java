/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.other;

import com.jogamp.opengl.GL2;
import jmodelling.engine.object.Object3D;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Axis extends Object3D{
    
    private static final float axisCoords[] = new float[]{
        0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
    };

    private static final float axisColors[] = new float[]{
        1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };
    
    public Axis(String name, Vec3f loc, Vec3f rot, Vec3f sca){
        super(name, loc, rot, sca);
    }

    @Override
    public void renderOpaque(GL2 gl) {
        gl.glPushMatrix();
        
        gl.glMultMatrixf(getLocalAxis().toArray(), 0);
        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < 6; i++) {
            gl.glColor3fv(axisColors, i * 3);
            gl.glVertex3fv(axisCoords, i * 3);
        }
        gl.glEnd();
        
        gl.glPopMatrix();
    }
    
    
    
}
