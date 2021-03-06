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
package jmodelling.engine.raytracing;

import java.nio.FloatBuffer;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Raytracer {

    //Möller–Trumbore intersection algorithm
    public static boolean rayIntersectsTriangle(Vec3f rayOrigin,
            Vec3f rayVector,
            Vec3f vertex0, Vec3f vertex1, Vec3f vertex2,
            Vec3f outIntersectionPoint) {
        final float EPSILON = 0.0000001f;
        Vec3f edge1 = vertex1.sub_(vertex0);
        Vec3f edge2 = vertex2.sub_(vertex0);
        Vec3f h = rayVector.cross_(edge2);
        float a, f, u, v;
        a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }
        f = 1.0f / a;
        Vec3f s = rayOrigin.sub_(vertex0);
        u = f * (s.dot(h));
        if (u < 0.0f || u > 1.0f) {
            return false;
        }
        Vec3f q = s.cross_(edge1);
        v = f * rayVector.dot(q);
        if (v < 0.0f || u + v > 1.0f) {
            return false;
        }
        // At this stage we can compute t to find out where the intersection point is on the line.
        float t = f * edge2.dot(q);
        if (t > EPSILON) { // ray intersection
            outIntersectionPoint.set(rayVector).scale(t).add(rayOrigin);
            return true;
        } else { // This means that there is a line intersection but not a ray intersection.
            return false;
        }
    }

    //Möller–Trumbore intersection algorithm
    public static boolean rayIntersectsTriangle(Vec3f rayOrigin,
            Vec3f rayVector,
            Vec3f vertex0, Vec3f vertex1, Vec3f vertex2, 
            float borderOffset,
            Vec3f outIntersectionPoint) {
        final float EPSILON = 0.0000001f;
        Vec3f edge1 = vertex1.sub_(vertex0);
        Vec3f edge2 = vertex2.sub_(vertex0);
        Vec3f h = rayVector.cross_(edge2);
        float a, f, u, v;
        a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }
        f = 1.0f / a;
        Vec3f s = rayOrigin.sub_(vertex0);
        u = f * (s.dot(h));
        if (u < -borderOffset || u > 1.0f + borderOffset) {
            return false;
        }
        Vec3f q = s.cross_(edge1);
        v = f * rayVector.dot(q);
        if (v < -borderOffset || u + v > 1.0f + borderOffset) {
            return false;
        }
        // At this stage we can compute t to find out where the intersection point is on the line.
        float t = f * edge2.dot(q);
        if (t > EPSILON) { // ray intersection
            outIntersectionPoint.set(rayVector).scale(t).add(rayOrigin);
            return true;
        } else { // This means that there is a line intersection but not a ray intersection.
            return false;
        }
    }

    public static boolean rayIntersectsTriangle(Vec3f rayOrigin,
            Vec3f rayVector,
            float[] vCoords, int offset,
            Vec3f outIntersectionPoint) {
        return rayIntersectsTriangle(rayOrigin, rayVector,
                new Vec3f(vCoords, offset),
                new Vec3f(vCoords, offset + 3),
                new Vec3f(vCoords, offset + 6),
                outIntersectionPoint);
    }

    public static boolean rayIntersectsTriangle(Vec3f rayOrigin,
            Vec3f rayVector,
            FloatBuffer vCoords, int offset,
            Vec3f outIntersectionPoint) {
        return rayIntersectsTriangle(rayOrigin, rayVector,
                new Vec3f(vCoords, offset),
                new Vec3f(vCoords, offset + 3),
                new Vec3f(vCoords, offset + 6),
                outIntersectionPoint);
    }

    public static float rayPointDistance(Vec3f rayPos, Vec3f rayDir, Vec3f point) {
        return rayDir.cross_(point.sub_(rayPos)).norm();
    }

    public static boolean rayIntersectsSphere(Vec3f rayPos, Vec3f rayDir,
            Vec3f sphPos, float sphRadius, float t, Vec3f q) {

        Vec3f m = rayPos.sub_(sphPos);
        float b = m.dot(rayDir);
        float c = m.dot(m) - sphRadius * sphRadius;

        // Exit if r’s origin outside s (c > 0) and r pointing away from s (b > 0) 
        if (c > 0.0f && b > 0.0f) {
            return false;
        }
        float discr = b * b - c;

        // A negative discriminant corresponds to ray missing sphere 
        if (discr < 0.0f) {
            return false;
        }

        return true;
        /*
        // Ray now found to intersect sphere, compute smallest t value of intersection
        t = -b - (float)Math.sqrt(discr);

        // If t is negative, ray started inside sphere so clamp t to zero 
        if (t < 0.0f) {
            t = 0.0f;
        }
        q = p + t * d;

        return 1;*/
    }

}
