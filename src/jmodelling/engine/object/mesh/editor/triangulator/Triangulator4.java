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
package jmodelling.engine.object.mesh.editor.triangulator;

import java.util.List;
import jmodelling.engine.object.mesh.editor.triangulator.CircularVtxList.CNode;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Triangulator4 {
    
    /*
    public static void earClipping(List<Vec3f> poly) {
        //Calculate the normal of the polygon
        Vec3f normal = getPolygonNormal(poly);

        //4 vertex lists of indices that point to the poly list
        CircularVtxList vtxs = new CircularVtxList();
        VtxList convxs = new VtxList();
        VtxList reflxs = new VtxList();
        VtxList ears = new VtxList();

        //Init the vertex indices
        for (int i = 0; i < poly.size(); i++) {
            vtxs.add(new VInd(i));
        }

        //Clasify the vertices into convex or reflex
        for (CircularVtxList.Iterator iteVtxs = vtxs.getIterator(); iteVtxs.hasNext(); iteVtxs.move()) {
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

            if (prev.item.list == reflxs) {
                if (isVertexConvex(poly, prev, normal)) {
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
    */

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

    private static boolean isVertexConvex(List<Vec3f> poly, CNode node, Vec3f polyNormal) {
        return isTriangleConvex(
                poly.get(node.prev.item.index),
                poly.get(node.item.index),
                poly.get(node.next.item.index),
                polyNormal);
    }

    private static boolean isTriangleConvex(Vec3f t1, Vec3f t2, Vec3f t3, Vec3f polyNormal) {
        return t2.sub_(t1).cross(t3.sub_(t2)).normalize().dot(polyNormal) > 0.0f;
    }
    
}
