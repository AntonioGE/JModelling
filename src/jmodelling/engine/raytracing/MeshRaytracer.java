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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.bounds.BoundingSphere;
import jmodelling.engine.object.camera.Cam;
import jmodelling.engine.object.mesh.MeshEditableObject;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.engine.object.mesh.cmesh.CShape;
import jmodelling.engine.object.mesh.cmesh.PolygonArray;
import jmodelling.engine.object.mesh.emesh.EMesh;
import jmodelling.engine.object.mesh.emesh.Edge;
import jmodelling.engine.object.mesh.emesh.Polygon;
import static jmodelling.engine.raytracing.Raytracer.rayIntersectsSphere;
import static jmodelling.engine.raytracing.Raytracer.rayIntersectsTriangle;
import static jmodelling.engine.raytracing.Raytracer.rayPointDistance;
import jmodelling.math.mat.Mat3f;
import jmodelling.math.mat.Mat4f;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;
import jmodelling.math.vec.Vec4f;
import jmodelling.utils.CollectionUtils;
import jmodelling.utils.collections.IdentitySet;

/**
 *
 * @author ANTONIO
 */
public class MeshRaytracer {

    public static boolean rayIntersectsCMeshLocal(Vec3f rayPosLocal,
            Vec3f rayDirLocal,
            MeshObject meshObject,
            Vec3f outIntersectionPoint) {

        Vec3f intersection = new Vec3f();
        float minDist = Float.MAX_VALUE;
        boolean intersectionFound = false;
        for (CShape shape : meshObject.cmesh.shapes.values()) {
            for (PolygonArray pArray : shape.polys.values()) {
                for (int i = 0; i < pArray.tris.length; i += 3) {
                    Vec3f v0 = pArray.getVtx(meshObject.cmesh, pArray.tris[i]);
                    Vec3f v1 = pArray.getVtx(meshObject.cmesh, pArray.tris[i + 1]);
                    Vec3f v2 = pArray.getVtx(meshObject.cmesh, pArray.tris[i + 2]);
                    if (rayIntersectsTriangle(rayPosLocal, rayDirLocal, v0, v1, v2, intersection)) {
                        intersectionFound = true;
                        float dist = intersection.dist(rayPosLocal);
                        if (dist < minDist) {
                            minDist = dist;
                            outIntersectionPoint.set(intersection);
                        }
                    }
                }
            }
        }
        return intersectionFound;
    }

    public static boolean rayIntersectsCMesh(Vec3f rayOrigin, Vec3f rayVector,
            MeshObject meshObject,
            Vec3f outIntersectionPoint) {
        Mat3f transf = meshObject.getRotationMatrix3f();
        Mat3f transfInv = transf.transp_();
        //Vec3f rayOriginTransf = rayOrigin.add_(meshObject.loc).mul(transf);
        Vec3f rayOriginTransf = rayOrigin.sub_(meshObject.loc).mul(transfInv).had(meshObject.sca.invert_());
        Vec3f rayVectorTransf = rayVector.mul_(transfInv);

        Vec3f intersection = new Vec3f();
        float minDist = Float.MAX_VALUE;
        boolean intersectionFound = false;
        for (CShape shape : meshObject.cmesh.shapes.values()) {
            for (PolygonArray pArray : shape.polys.values()) {
                for (int i = 0; i < pArray.tris.length; i += 3) {
                    Vec3f v0 = pArray.getVtx(meshObject.cmesh, pArray.tris[i]);
                    Vec3f v1 = pArray.getVtx(meshObject.cmesh, pArray.tris[i + 1]);
                    Vec3f v2 = pArray.getVtx(meshObject.cmesh, pArray.tris[i + 2]);
                    if (rayIntersectsTriangle(rayOriginTransf, rayVectorTransf, v0, v1, v2, intersection)) {
                        intersectionFound = true;
                        float dist = intersection.dist(rayOriginTransf);
                        if (dist < minDist) {
                            minDist = dist;
                            outIntersectionPoint.set(intersection);
                        }
                    }
                }
            }
        }

        if (intersectionFound) {
            outIntersectionPoint.had(meshObject.sca).mul(transf).add(meshObject.loc);
            return true;
        } else {
            return false;
        }
    }

    public static boolean rayIntersectsBoundingSphereLocal(Vec3f rayPosLocal,
            Vec3f rayDirLocal,
            Object3D obj) {

        BoundingSphere sphere = obj.getBoundingSphere();
        if (sphere != null) {
            return rayIntersectsSphere(rayPosLocal, rayDirLocal, sphere.center, sphere.radius, 0, new Vec3f());
        } else {
            return false;
        }

    }

    private static Vec3f rayPosToLocal(Vec3f rayPos, Object3D obj, Mat3f transfInv) {
        return rayPos.sub_(obj.loc).mul(transfInv).had(obj.sca.invert_());
    }

    private static Vec3f rayDirToLocal(Vec3f rayDir, Object3D obj, Mat3f transfInv) {
        return rayDir.mul_(transfInv).had(obj.sca.invert_()).normalize();
    }

    private static Ray rayToLocal(Ray ray, Object3D obj, Mat3f transfInv) {
        return new Ray(ray.loc.sub_(obj.loc).mul(transfInv).had(obj.sca.invert_()),
                ray.dir.mul_(transfInv).had(obj.sca.invert_()).normalize());
    }

    public static MeshObject getSelectedMeshObject(Vec3f rayPos, Vec3f rayDir,
            Set<MeshObject> objects) {

        HashMap<MeshObject, Vec3f> meshIntersections = new HashMap<>();
        for (MeshObject obj : objects) {
            if (obj.isSelectable()) {
                Mat3f transf = obj.getRotationMatrix3f();
                Mat3f transfInv = transf.transp_();

                Vec3f rayPosLocal = rayPosToLocal(rayPos, obj, transfInv);
                Vec3f rayDirLocal = rayDirToLocal(rayDir, obj, transfInv);

                if (rayIntersectsBoundingSphereLocal(rayPosLocal, rayDirLocal, obj)) {
                    Vec3f intersection = new Vec3f();
                    if (rayIntersectsCMeshLocal(rayPosLocal, rayDirLocal, obj, intersection)) {
                        intersection.had(obj.sca).mul(transf).add(obj.loc);
                        meshIntersections.put(obj, intersection);
                    }
                }
            }
        }

        float minDist = Float.MAX_VALUE;
        MeshObject closestObject = null;
        for (Map.Entry<MeshObject, Vec3f> entry : meshIntersections.entrySet()) {
            float dist = entry.getValue().dist(rayPos);
            if (dist < minDist) {
                minDist = dist;
                closestObject = entry.getKey();
            }
        }

        return closestObject;
    }

    public static List<MeshObject> getIntersectingMeshObjects(Vec3f rayPos, Vec3f rayDir,
            Set<MeshObject> objects) {

        HashMap<MeshObject, Vec3f> meshIntersections = new HashMap<>();
        objects.stream().filter((obj) -> (obj.isSelectable())).forEachOrdered((obj) -> {
            Mat3f transf = obj.getRotationMatrix3f();
            Mat3f transfInv = transf.transp_();

            Vec3f rayPosLocal = rayPosToLocal(rayPos, obj, transfInv);
            Vec3f rayDirLocal = rayDirToLocal(rayDir, obj, transfInv);
            if (rayIntersectsBoundingSphereLocal(rayPosLocal, rayDirLocal, obj)) {
                Vec3f intersection = new Vec3f();
                if (rayIntersectsCMeshLocal(rayPosLocal, rayDirLocal, obj, intersection)) {
                    intersection.had(obj.sca).mul(transf).add(obj.loc);
                    meshIntersections.put(obj, intersection);
                }
            }
        });

        TreeSet<Map.Entry<MeshObject, Vec3f>> sortedByDist = new TreeSet<>(new Comparator<Map.Entry<MeshObject, Vec3f>>() {
            @Override
            public int compare(Map.Entry<MeshObject, Vec3f> s1, Map.Entry<MeshObject, Vec3f> s2) {
                return Float.compare(s1.getValue().dist(rayPos), s2.getValue().dist(rayPos));
            }
        });
        sortedByDist.addAll(meshIntersections.entrySet());

        List<MeshObject> sortedList = new ArrayList<>(sortedByDist.size());
        sortedByDist.forEach((entry) -> {
            sortedList.add(entry.getKey());
        });

        return sortedList;
    }

    public static List<MeshObject> getIntersectingMeshObjects(Ray ray,
            Set<MeshObject> objects) {
        return getIntersectingMeshObjects(ray.loc, ray.dir, objects);
    }

    public static List<Vec3f> getIntersectingVtxs(Vec3f rayPosLocal, Vec3f rayDirLocal,
            Collection<Vec3f> vtxs, float minDist) {
        IdentityHashMap<Vec3f, Float> intersections = new IdentityHashMap<>();
        for (Vec3f vtx : vtxs) {
            float dist = rayPointDistance(rayPosLocal, rayDirLocal, vtx);
            if (dist < minDist) {
                intersections.put(vtx, dist);
            }
        }

        TreeSet<Map.Entry<Vec3f, Float>> sortedByDist = new TreeSet<>(new Comparator<Map.Entry<Vec3f, Float>>() {
            @Override
            public int compare(Map.Entry<Vec3f, Float> s1, Map.Entry<Vec3f, Float> s2) {
                return Float.compare(s1.getValue(), s2.getValue());
            }
        });
        sortedByDist.addAll(intersections.entrySet());

        List<Vec3f> sortedList = new ArrayList<>(sortedByDist.size());
        sortedByDist.forEach((entry) -> {
            sortedList.add(entry.getKey());
        });

        return sortedList;
    }

    public static List<Vec3f> getIntersectingVtxs(Vec2f rayPos,
            Collection<Vec3f> vtxs, Mat4f objMat, Mat4f camMVP, float radius) {

        IdentityHashMap<Vec3f, Float> intersections = new IdentityHashMap<>();
        Mat4f transf = camMVP.mul_(objMat);
        for (Vec3f vtx : vtxs) {
            Vec3f projVec = new Vec4f(vtx, 1.0f).mul(transf).toVec3f();
            if (rayPos.dist(new Vec2f(projVec.x, projVec.y)) < radius) {
                intersections.put(vtx, projVec.z);
            }
        }

        TreeSet<Map.Entry<Vec3f, Float>> sortedByDist = new TreeSet<>(new Comparator<Map.Entry<Vec3f, Float>>() {
            @Override
            public int compare(Map.Entry<Vec3f, Float> s1, Map.Entry<Vec3f, Float> s2) {
                return Float.compare(s1.getValue(), s2.getValue());
            }
        });
        sortedByDist.addAll(intersections.entrySet());

        List<Vec3f> sortedList = new ArrayList<>(sortedByDist.size());
        sortedByDist.forEach((entry) -> {
            sortedList.add(entry.getKey());
        });

        return sortedList;
    }

    public static List<Vec3f> rayEMeshIntersectionsLocal(Ray rayLocal,
            EMesh emesh) {

        TreeSet<Vec3f> intersections = new TreeSet<>(new Comparator<Vec3f>() {
            @Override
            public int compare(Vec3f o1, Vec3f o2) {
                return Float.compare(rayLocal.loc.dist(o1), rayLocal.loc.dist(o2));
            }
        });

        for (Polygon poly : emesh.polys) {
            for (int i = 0; i < poly.tris.size(); i += 3) {
                Vec3f intersection = new Vec3f();
                if (Raytracer.rayIntersectsTriangle(rayLocal.loc, rayLocal.dir,
                        poly.tris.get(i).vtx, poly.tris.get(i + 1).vtx,
                        poly.tris.get(i + 2).vtx, 0.0001f, intersection)) {
                    intersections.add(intersection);
                }
            }
        }

        List<Vec3f> sortedList = new ArrayList<>(intersections.size());
        for (Vec3f v : intersections) {
            sortedList.add(v);
        }
        return sortedList;
    }

    public static List<Vec3f> rayEMeshIntersections(Ray ray,
            MeshEditableObject obj) {
        Mat3f transf = obj.getRotationMatrix3f();
        Mat3f transfInv = transf.transp_();

        Ray rayLocal = rayToLocal(ray, obj, transfInv);

        return rayEMeshIntersectionsLocal(rayLocal, obj.emesh);
    }

    public static Vec3f getVtxSolidLocal(Ray rayLocal, Vec2f rayView,
            MeshEditableObject obj, Mat4f camMVP,
            float radius, float minDist) {

        IdentityHashMap<Vec3f, Float> closeVtxs = new IdentityHashMap<>();
        Mat4f transf = camMVP.mul_(obj.getModelMatrix());
        for (Vec3f vtx : obj.emesh.vtxs) {
            Vec3f projVec = new Vec4f(vtx, 1.0f).mul(transf).toVec3f();
            float dist = rayView.dist(new Vec2f(projVec.x, projVec.y));
            if (dist < radius) {
                closeVtxs.put(vtx, dist);
            }
        }

        if (closeVtxs.size() > 0) {
            TreeSet<Vec3f> sortedVtxs = new TreeSet<>(new Comparator<Vec3f>() {
                @Override
                public int compare(Vec3f o1, Vec3f o2) {
                    return Float.compare(closeVtxs.get(o1), closeVtxs.get(o2));
                }
            });
            for (Vec3f vtx : closeVtxs.keySet()) {
                sortedVtxs.add(vtx);
            }

            for (Vec3f vtx : sortedVtxs) {
                Ray ray = Ray.newRayTwoPoints(rayLocal.loc, vtx);
                List<Vec3f> inters = rayEMeshIntersectionsLocal(ray, obj.emesh);
                if (inters.size() > 0) {
                    if (inters.get(0).dist(vtx) < minDist) {
                        return vtx;
                    }
                }
            }
        }
        return null;
    }

    public static Vec3f getVtxSolid(Ray ray, Vec2f rayView,
            MeshEditableObject obj, Mat4f camMVP,
            float radius, float minDist) {

        Mat3f transf = obj.getRotationMatrix3f();
        Mat3f transfInv = transf.transp_();
        Ray rayLocal = rayToLocal(ray, obj, transfInv);

        return getVtxSolidLocal(rayLocal, rayView, obj, camMVP, radius, minDist);
    }

    //TODO: This method is not 100% reliable. It has to be improved
    public static Edge getEdgeSolidLocal(Ray rayLocal, Vec2f rayView,
            MeshEditableObject obj, Mat4f camMVP,
            float radius, float minDist) {

        IdentityHashMap<Vec3f, Vec2f> projVtxs = CollectionUtils.newIdentityHashMap(obj.emesh.vtxs.size());
        Mat4f transf = camMVP.mul_(obj.getModelMatrix());
        for (Vec3f vtx : obj.emesh.vtxs) {
            Vec4f projVec = new Vec4f(vtx, 1.0f).mul(transf);
            projVtxs.put(vtx, new Vec2f(projVec.x / projVec.w, projVec.y / projVec.w));
        }

        IdentityHashMap<Edge, Float> closeEdges = new IdentityHashMap<>();
        for (Edge edge : obj.emesh.edges) {
            float dist = rayView.distToSegment(projVtxs.get(edge.v0), projVtxs.get(edge.v1));
            if (dist < radius) {
                closeEdges.put(edge, dist);
            }
        }

        if (closeEdges.size() > 0) {
            TreeSet<Edge> sortedEdges = new TreeSet<>(new Comparator<Edge>() {
                @Override
                public int compare(Edge o1, Edge o2) {
                    return Float.compare(closeEdges.get(o1), closeEdges.get(o2));
                }
            });
            for (Edge edge : closeEdges.keySet()) {
                sortedEdges.add(edge);
            }

            for (Edge edge : sortedEdges) {
                Ray ray = Ray.newRayTwoPoints(rayLocal.loc, edge.v0);
                List<Vec3f> inters = rayEMeshIntersectionsLocal(ray, obj.emesh);
                for(Vec3f v : inters){
                    v.print("INTER");
                }
                if (inters.size() > 0) {
                    if (inters.get(0).dist(edge.v0) < minDist) {
                        return edge;
                    } 
                }
            }
        }
        return null;
    }

    public static Edge getEdgeSolid(Ray ray, Vec2f rayView,
            MeshEditableObject obj, Mat4f camMVP,
            float radius, float minDist) {

        Mat3f transf = obj.getRotationMatrix3f();
        Mat3f transfInv = transf.transp_();
        Ray rayLocal = rayToLocal(ray, obj, transfInv);

        return getEdgeSolidLocal(rayLocal, rayView, obj, camMVP, radius, minDist);
    }

    //TODO: Remove this temporary function
    public static Vec3f getClosestIntersectionPoint(Vec3f rayPos, Vec3f rayDir,
            Set<MeshObject> objects) {

        int count = 0;
        HashMap<MeshObject, Vec3f> meshIntersections = new HashMap<>();
        for (MeshObject obj : objects) {
            if (obj.isSelectable()) {
                Mat3f transf = obj.getRotationMatrix3f();
                Mat3f transfInv = transf.transp_();

                Vec3f rayPosLocal = rayPos.sub_(obj.loc).mul(transfInv).had(obj.sca.invert_());
                Vec3f rayDirLocal = rayDir.mul_(transfInv).had(obj.sca.invert_()).normalize();

                if (rayIntersectsBoundingSphereLocal(rayPosLocal, rayDirLocal, obj)) {
                    count++;
                    Vec3f intersection = new Vec3f();
                    if (rayIntersectsCMeshLocal(rayPosLocal, rayDirLocal, obj, intersection)) {
                        intersection.had(obj.sca).mul(transf).add(obj.loc);
                        meshIntersections.put(obj, intersection);
                        //System.out.println(obj.name + " " + intersection.toString());
                    }
                }
            }
        }

        System.out.println("SPHERE INTERSECTIONS: " + count);

        float minDist = Float.MAX_VALUE;
        MeshObject closestObject = null;
        for (Map.Entry<MeshObject, Vec3f> entry : meshIntersections.entrySet()) {
            float dist = entry.getValue().dist(rayPos);
            if (dist < minDist) {
                minDist = dist;
                closestObject = entry.getKey();
            }
        }

        return meshIntersections.get(closestObject);
    }
}
