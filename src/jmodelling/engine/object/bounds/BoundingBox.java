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
package jmodelling.engine.object.bounds;

import jmodelling.engine.object.newmesh.Mesh;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class BoundingBox {

    public Vec3f min;
    public Vec3f max;

    public BoundingBox(Mesh mesh) {
        if(mesh.vtxs.size() < 2){
            min = new Vec3f();
            max = new Vec3f();
            return;
        }
        
        min = new Vec3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        max = new Vec3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
        for (Vec3f v : mesh.vtxs) {
            if(v.x < min.x){
                min.x = v.x;
            }else if(v.x > max.x){
                max.x = v.x;
            }
            
            if(v.y < min.y){
                min.y = v.y;
            }else if(v.y > max.y){
                max.y = v.y;
            }
            
            if(v.z < min.z){
                min.z = v.z;
            }else if(v.y > max.z){
                max.z = v.z;
            }
        }
    }

}
