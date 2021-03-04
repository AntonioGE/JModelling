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
package jmodelling.engine.object.hud;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import java.nio.FloatBuffer;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.bounds.BoundingSphere;

/**
 *
 * @author ANTONIO
 */
public class Axis extends Object3D{
    
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

    final float scale = 100.0f;
    
    public Axis(){
        super("Axis");
    }

    @Override
    public void renderOpaque(GL2 gl) {
        gl.glPushMatrix();

        gl.glMultMatrixf(getLocalAxis().toArray(), 0);

        gl.glScalef(scale, scale, scale);
        
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glColorPointer(3, GL2.GL_FLOAT, 0, axisColorsBuff);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, axisCoordsBuff);

        gl.glDrawArrays(GL2.GL_LINES, 0, axisCoordsBuff.limit() / 3);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);

        gl.glPopMatrix();
    }

    @Override
    public void init(GL2 gl) {
    }

    @Override
    public void update(GL2 gl) {
    }

    @Override
    public void delete(GL2 gl) {
    }

    @Override
    public BoundingSphere getBoundingSphere() {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public String getType() {
        return "AXIS";
    }
}
