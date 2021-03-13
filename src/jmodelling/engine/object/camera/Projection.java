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

import jmodelling.math.mat.Mat4f;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public abstract class Projection {
    
    public abstract Vec3f viewPosToRay(CamArcball cam, Vec2f posView);
    
    public abstract Mat4f getProjectionMatrix(CamArcball cam, float aspect);
    
    public Vec3f viewPosToRayAspect(CamArcball cam, Vec2f posView, float aspect) {
        Vec2f posViewAspect = new Vec2f(posView);
        posViewAspect.x *= aspect;
        return viewPosToRay(cam, posViewAspect);
    }

    public Vec3f viewPosToRay(CamArcball cam, int xMouse, int yMouse, int screenWidth, int screenHeight) {
        return viewPosToRay(cam, Cam.pixelToViewAspect(xMouse, yMouse, screenWidth, screenHeight));
    }
    
}
