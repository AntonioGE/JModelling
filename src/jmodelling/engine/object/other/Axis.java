/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.other;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import java.nio.FloatBuffer;
import jmodelling.engine.object.Object3D;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Axis extends Object3D {
    
    private static final FloatBuffer axisCoordsBuff = Buffers.newDirectFloatBuffer(new float[]{
        0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
    });
    
    private static final FloatBuffer axisColorsBuff = Buffers.newDirectFloatBuffer(new float[]{
        1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    });

    public Axis(String name, Vec3f loc, Vec3f rot, Vec3f sca) {
        super(name, loc, rot, sca);
    }

    @Override
    public void renderOpaque(GL2 gl) {
        gl.glPushMatrix();

        gl.glMultMatrixf(getLocalAxis().toArray(), 0);
        
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glColorPointer(3, GL2.GL_FLOAT, 0, axisColorsBuff);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, axisCoordsBuff);

        gl.glDrawArrays(GL2.GL_LINES, 0, axisCoordsBuff.limit() / 3);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        
        gl.glPopMatrix();
        
        //gl.glTranslatef(loc.x, loc.y, loc.z);
        //gl.glRotatef(rot.z, 0.0f, 0.0f, 1.0f);
        //gl.glRotatef(rot.y, 0.0f, 1.0f, 0.0f);
        //gl.glRotatef(rot.x, 1.0f, 0.0f, 0.0f);
        
        //gl.glMultMatrixf(transf, 0);

        /*
        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < 6; i++) {
            gl.glColor3fv(axisColors, i * 3);
            gl.glVertex3fv(axisCoords, i * 3);
        }
        gl.glEnd();
        */
        
        
    }

}
