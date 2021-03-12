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
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Grid extends Object3D {

    private final FloatBuffer vtxs;
    private final FloatBuffer clrs;
    private final Vec3f xAxisColor = new Vec3f(0.5f, 0.02f, 0.02f);
    private final Vec3f yAxisColor = new Vec3f(0.02f, 0.5f, 0.02f);
    private final Vec3f gridColor = new Vec3f(0.29f, 0.29f, 0.29f);

    public Grid(int lines, float spacing) {
        super("GRID", new Vec3f());

        final int buffSize = (lines + 1) * 2 * 2 * 3;
        vtxs = Buffers.newDirectFloatBuffer(buffSize);
        clrs = Buffers.newDirectFloatBuffer(buffSize);
        vtxs.mark();
        clrs.mark();

        final int first = -lines / 2;
        final int last = lines / 2;
        for (int i = first; i <= last; i++) {
            vtxs.put(first * spacing);
            vtxs.put(i * spacing);
            vtxs.put(0.0f);

            vtxs.put(last * spacing);
            vtxs.put(i * spacing);
            vtxs.put(0.0f);

            if (i == 0) {
                xAxisColor.writeInBuffer(clrs);
                xAxisColor.writeInBuffer(clrs);
            } else {
                gridColor.writeInBuffer(clrs);
                gridColor.writeInBuffer(clrs);
            }
        }
        for (int i = first; i <= last; i++) {
            vtxs.put(i * spacing);
            vtxs.put(first * spacing);
            vtxs.put(0.0f);

            vtxs.put(i * spacing);
            vtxs.put(last * spacing);
            vtxs.put(0.0f);

            if (i == 0) {
                yAxisColor.writeInBuffer(clrs);
                yAxisColor.writeInBuffer(clrs);
            } else {
                gridColor.writeInBuffer(clrs);
                gridColor.writeInBuffer(clrs);
            }
        }

        vtxs.reset();
        clrs.reset();
    }

    @Override
    public void renderOpaque(GL2 gl) {
        gl.glPushMatrix();

        gl.glMultMatrixf(getModelMatrix().toArray(), 0);

        gl.glColor3f(0.29f, 0.29f, 0.29f);

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        gl.glColorPointer(3, GL2.GL_FLOAT, 0, clrs);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vtxs);
        gl.glDrawArrays(GL2.GL_LINES, 0, vtxs.limit() / 3);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        gl.glPopMatrix();
    }

    @Override
    public void renderWireframe(GL2 gl) {

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
        return "GRID";
    }

}
