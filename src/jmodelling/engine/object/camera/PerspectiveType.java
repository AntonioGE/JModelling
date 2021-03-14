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

import jmodelling.engine.raytracing.Ray;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.transf.TransfMat;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class PerspectiveType extends CamType {

    @Override
    public Ray viewPosToRay(CamArcball cam, Vec2f posView) {
        final float tan = (float) Math.tan(Math.toRadians(cam.fov / 2.0f));
        final Vec3f dir = new Vec3f(posView.x * tan, posView.y * tan, -1.0f).normalize().mul(cam.getRotationMatrix3f());
        return new Ray(cam.loc.clone(), dir);
    }

    @Override
    public Mat4f getProjectionMatrix(CamArcball cam, float aspect) {
        return TransfMat.perspective_(cam.fov, aspect, cam.zNear, cam.zFar);
    }

    @Override
    public void zoom(CamArcball cam, float delta) {
        //Get target position before rotation
        Vec3f tar = cam.getTar();

        //Update distance to target
        cam.distToTarget *= delta;

        //Move camera location
        cam.loc = tar.add(cam.getDir().negate().scale(cam.distToTarget));
    }

    @Override
    public Mat4f getModelViewMatrix(CamArcball cam) {
        Mat4f rx = TransfMat.rotationDeg_(-cam.rot.x, new Vec3f(1.0f, 0.0f, 0.0f));
        Mat4f ry = TransfMat.rotationDeg_(-cam.rot.y, new Vec3f(0.0f, 1.0f, 0.0f));
        Mat4f rz = TransfMat.rotationDeg_(-cam.rot.z, new Vec3f(0.0f, 0.0f, 1.0f));
        Mat4f t = TransfMat.translation_(cam.loc.negate_());
        return rx.mul_(ry).mul(rz).mul(t);
    }

    @Override
    public void translate(CamArcball cam, Vec2f deltaView) {
        Vec3f trans = new Vec3f(
                deltaView.x * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)),
                deltaView.y * cam.distToTarget * (float) Math.tan(Math.toRadians(cam.fov / 2.0f)),
                0.0f
        );
        cam.loc.add(trans.mul(cam.getRotationMatrix3f()));
    }
    
    

}
