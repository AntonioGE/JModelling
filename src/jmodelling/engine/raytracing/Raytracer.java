/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.raytracing;

import jmodelling.engine.object.mesh.face.Tri;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Raytracer {

    //Möller–Trumbore intersection algorithm
    public static boolean rayIntersectsTriangle(Vec3f rayOrigin,
            Vec3f rayVector,
            Tri inTriangle,
            Vec3f outIntersectionPoint) {
        final float EPSILON = 0.0000001f;
        Vec3f vertex0 = inTriangle.vertices.get(0).pos;
        Vec3f vertex1 = inTriangle.vertices.get(1).pos;
        Vec3f vertex2 = inTriangle.vertices.get(2).pos;
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
        if (t > EPSILON) // ray intersection
        {
            outIntersectionPoint.set(rayVector).scale(t).add(rayOrigin);
            return true;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return false;
        }
    }

    public Vec3f rayIntersectsTriangle_(Vec3f rayOrigin, Vec3f rayVector, Tri inTriangle) {
        Vec3f intersectionPoint = new Vec3f();
        rayIntersectsTriangle(rayOrigin, rayVector, inTriangle, intersectionPoint);
        return intersectionPoint;
    }
}
