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
package jmodelling.engine.object.mesh.editor;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.collections.CircularLinkedList;
import jmodelling.utils.collections.CircularLinkedList.Node;
import jmodelling.utils.collections.CircularLinkedListOld;

/**
 *
 * @author ANTONIO
 */
public class Triangulator3 {

    public static void earClipping(List<Vec3f> poly) {
        //Calculate the normal of the polygon
        Vec3f normal = getPolygonNormal(poly);

        //4 vertex lists of indices that point to the poly list
        CircularLinkedList<VInd> vtxs = new CircularLinkedList<>();
        LinkedList<CircularLinkedList<VInd>.Node<VInd>> convxs = new LinkedList<>();
        LinkedList<CircularLinkedList<VInd>.Node<VInd>> reflxs = new LinkedList<>();
        LinkedList<CircularLinkedList<VInd>.Node<VInd>> ears = new LinkedList<>();

        //Init the vertex indices
        for (int i = 0; i < poly.size(); i++) {
            vtxs.add(new VInd(i));
        }

        //Clasify the vertices into convex or reflex
        for (CircularLinkedList<VInd>.CircularIterator<VInd> iteVtxs = vtxs.getIterator(); iteVtxs.hasNext(); iteVtxs.move()) {
            if (isVertexConvex(poly, iteVtxs.getCurrentNode(), normal)) {
                convxs.add(iteVtxs.getCurrentNode());
                iteVtxs.getCurrentNode().item.list = convxs;
            } else {
                reflxs.add(iteVtxs.getCurrentNode());
                iteVtxs.getCurrentNode().item.list = reflxs;
            }
        }

        //Extract the ear vertices from the convex vertices
        for (ListIterator<CircularLinkedList<VInd>.Node<VInd>> iteConvx = convxs.listIterator(); iteConvx.hasNext();) {
            CircularLinkedList<VInd>.Node<VInd> convx = iteConvx.next();
            if (isVertexEar(poly, convx, reflxs)) {
                ears.add(convx);
                convx.item.list = ears;
                //iteConvx.remove();
            }
        }

        //Create a triangle list for storing the triangulated polygon as vertex 
        //indices that point to the original list
        List<Integer> tris = new ArrayList<>((poly.size() - 2) * 3);

        //Extract all the ears and add them to the triangle list
        for (ListIterator<CircularLinkedList<VInd>.Node<VInd>> iteEars = ears.listIterator(); iteEars.hasNext();) {
            CircularLinkedList<VInd>.Node<VInd> ear = iteEars.next();

            CircularLinkedList<VInd>.Node<VInd> prev = ear.prev;
            CircularLinkedList<VInd>.Node<VInd> next = ear.next;

            tris.add(prev.item.index);
            tris.add(ear.item.index);
            tris.add(next.item.index);

            vtxs.remove(ear);

            if(prev.item.list == reflxs) {
                if(isVertexConvex(poly, prev, normal)){
                    reflxs.remove(prev);
                }
            }
            if (isVertexConvex(poly, prev, normal)) {
                convxs.add(prev);
            } else {
                reflxs.add(prev);
            }

        }

    }

    private static boolean isVertexConvex(List<Vec3f> poly, CircularLinkedList<VInd>.Node<VInd> node, Vec3f polyNormal) {
        return isTriangleConvex(
                poly.get(node.prev.item.index),
                poly.get(node.item.index),
                poly.get(node.next.item.index),
                polyNormal);
    }

    private static boolean isTriangleConvex(Vec3f t1, Vec3f t2, Vec3f t3, Vec3f polyNormal) {
        return t2.sub_(t1).cross(t3.sub_(t2)).normalize().dot(polyNormal) > 0.0f;
    }

    private static boolean isVertexEar(List<Vec3f> poly,
            CircularLinkedList<VInd>.Node<VInd> vertexNode,
            LinkedList<CircularLinkedList<VInd>.Node<VInd>> reflxs) {
        //Triangle vertices ordered
        Vec3f t1 = poly.get(vertexNode.prev.item.index);
        Vec3f t2 = poly.get(vertexNode.item.index);
        Vec3f t3 = poly.get(vertexNode.next.item.index);

        //Check if the triangle contains any projection of the reflex vertices
        Vec3f u = t2.sub_(t1);
        Vec3f v = t3.sub_(t1);
        Vec3f n = u.cross_(v);
        float n2 = n.dot(n);
        for (CircularLinkedList<VInd>.Node<VInd> node : reflxs) {
            Vec3f vtx = poly.get(node.item.index);
            if ((vtx != t1) && (vtx != t3)) {
                Vec3f w = vtx.sub_(t1);
                if (isInsideTriangle(u, v, w, n, n2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isInsideTriangle(Vec3f u, Vec3f v, Vec3f w, Vec3f n, float n2) {
        final float gamma = u.cross_(w).dot(n) / n2;
        if (gamma >= 0.0f && gamma <= 1.0f) {
            final float beta = w.cross_(v).dot(n) / n2;
            if (beta >= 0.0f && beta <= 1.0f) {
                final float alpha = 1.0f - gamma - beta;
                if (alpha >= 0.0f && beta <= 1.0f) {
                    return true;
                }
            }
        }
        return false;
    }

    //Newell's method
    private static Vec3f getPolygonNormal(List<Vec3f> vtxs) {
        Vec3f normal = new Vec3f();

        for (int i = 0; i < vtxs.size(); i++) {
            Vec3f c = vtxs.get(i);
            Vec3f n = vtxs.get((i + 1) % vtxs.size());
            normal.x += (c.y - n.y) * (c.z + n.z);
            normal.y += (c.z - n.z) * (c.x + n.x);
            normal.z += (c.x - n.x) * (c.y + n.y);
        }
        return normal.normalize();
    }

    private static class VInd {
        int index;
        LinkedList list;
        
        public VInd(int index){
            this.index = index;
            this.list = null;
        }
    };
    
}
