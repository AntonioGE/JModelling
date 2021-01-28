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
package jmodelling.engine.object.camera;

import com.jogamp.opengl.GL2;
import jmodelling.engine.object.Object3D;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public abstract class Cam extends Object3D {

    public Cam(String name, Vec3f loc, Vec3f rot) {
        super(name, loc, rot);
    }

    public static void rotToDir(Vec3f rot, Vec3f dir) {
        dir.mul(TransfMat.eulerToMat_(rot));

        //Alternate method:
        //dir.set(rot.add_(90.0f, 0.0f, -90.0f).anglesXZDegToVector());
        //dir.z = -dir.z;
    }

    public static Vec3f rotToDir_(Vec3f angles) {
        Vec3f dir = new Vec3f(0.0f, 0.0f, -1.0f);
        rotToDir(angles, dir);
        return dir;
    }

    public static void dirToRot(Vec3f dir, Vec3f rot) {
        rot.set(dir.anglesXZDeg_());
        rot.add(new Vec3f(90.0f, 0.0f, -90.0f));
    }

    public static Vec3f dirToRot_(Vec3f dir) {
        Vec3f rot = new Vec3f();
        dirToRot(dir, rot);
        return rot;
    }

    public Vec3f getDir() {
        return rotToDir_(rot);
    }

    public void setDir(Vec3f dir) {
        dirToRot(dir, rot);
    }

    /**
     * Converts pixel screen coordinates to view coordinates
     * 
     * @param xMouse pixel X coordiante
     * @param yMouse pixel Y coordinate
     * @param screenWidth screen width in pixels
     * @param screenHeight screen height in pixels
     * @return XY coordinates in view coordinates
     */
    public static Vec2f pixelToView(int xMouse, int yMouse, int screenWidth, int screenHeight) {
        final float aspect = (float) screenWidth / screenHeight;
        return new Vec2f(
                ((float) xMouse / (screenWidth / 2) - 1.0f),
                -((float) yMouse / (screenHeight / 2) - 1.0f) * aspect);
    }

    @Override
    public abstract void renderOpaque(GL2 gl);
}
