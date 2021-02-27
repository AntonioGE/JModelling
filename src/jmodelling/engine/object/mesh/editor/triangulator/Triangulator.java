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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.collections.nodes.CircularLinkList;
import jmodelling.utils.collections.nodes.Node;
import jmodelling.utils.collections.nodes.NodeIterator;

/**
 *
 * @author ANTONIO
 */
public class Triangulator {

    public static List<Integer> earClipping(List<Vec3f> poly) {
        //Calculate the normal of the polygon
        final Vec3f normal = getPolygonNormal(poly);

        //4 vertex lists of indices that point to the poly list
        final CircularLinkList<VInd> vtxs = new CircularLinkList();
        final CircularLinkList<Node<VInd>> convxs = new CircularLinkList();
        final CircularLinkList<Node<VInd>> reflxs = new CircularLinkList();
        final CircularLinkList<Node<VInd>> ears = new CircularLinkList();

        //Init the vertex indices
        for (int i = 0; i < poly.size(); i++) {
            vtxs.add(new VInd(i));
        }

        //Clasify the vertices into convex or reflex
        for (NodeIterator<VInd> iteVtxs = vtxs.iterator(); iteVtxs.hasNext(); iteVtxs.move()) {
            final Node<VInd> node = iteVtxs.getCurrentNode();
            if (isVertexConvex(poly, node, normal)) {
                convxs.add(node);
                node.item.list = convxs;
                node.item.node = convxs.getLastNode();
            } else {
                reflxs.add(node);
                node.item.list = reflxs;
                node.item.node = reflxs.getLastNode();
            }
        }

        print("CONVX", convxs);
        print("RFLXS", reflxs);
        print("EARS", ears);

        //Extract the ear vertices from the convex vertices
        for (NodeIterator<Node<VInd>> iteConvx = convxs.iterator(); iteConvx.hasNext(); iteConvx.move()) {
            final Node<Node<VInd>> convx = iteConvx.getCurrentNode();
            if (isVertexEar(poly, convx.item, reflxs)) {
                ears.add(convx.item);
                convx.item.item.list = ears;
                convx.item.item.node = ears.getLastNode();
                iteConvx.remove();
            }
        }

        print("CONVX", convxs);
        print("RFLXS", reflxs);
        print("EARS", ears);
        
        //Create a triangle list for storing the triangulated polygon as vertex 
        //indices that point to the original list
        final List<Integer> tris = new ArrayList<>((poly.size() - 2) * 3);

        //Extract all the ears and add them to the triangle list
        //for (NodeIterator<Node<VInd>> iteEars = ears.iterator(); iteEars.hasNext(); iteEars.move()) {
        while (ears.size() > 0 && vtxs.size() > 3) {
            //final Node<Node<VInd>> node = ears.getFirstNode();
            final Node<VInd> ear = ears.getFirstNode().item;
            final Node<VInd> prev = ear.prev;
            final Node<VInd> next = ear.next;

            tris.add(prev.item.index);
            tris.add(ear.item.index);
            tris.add(next.item.index);

            //print("CONVX", convxs);
            //print("RFLXS", reflxs);
            //print("EARS", ears);
            System.out.println("Tri: " + prev.item.index + " " + ear.item.index + " " + next.item.index);
            //System.out.println();
            
            vtxs.remove(ear);

            updateReflxs(prev.item.node, poly, normal, convxs, reflxs);
            updateReflxs(next.item.node, poly, normal, convxs, reflxs);

            updateConvxsAndEars(prev.item.node, poly, convxs, reflxs, ears);
            updateConvxsAndEars(next.item.node, poly, convxs, reflxs, ears);

            ears.remove(ear.item.node);
        }

        for (NodeIterator<VInd> iteVtxs = vtxs.iterator(); iteVtxs.hasNext(); iteVtxs.move()) {
            final Node<VInd> node = iteVtxs.getCurrentNode();
            tris.add(node.item.index);
        }
        return tris;
    }

    private static void print(String name, CircularLinkList<Node<VInd>> list) {
        System.out.print(name);
        for (NodeIterator<Node<VInd>> ite = list.iterator(); ite.hasNext(); ite.move()) {
            final Node<VInd> node = ite.getCurrent();
            System.out.print(" " + node.item.index);
        }
        System.out.println();
    }

    private static void updateReflxs(Node<Node<VInd>> node, List<Vec3f> poly, Vec3f normal,
            CircularLinkList<Node<VInd>> convxs,
            CircularLinkList<Node<VInd>> reflxs) {
        if (node.item.item.list == reflxs && isVertexConvex(poly, node.item, normal)) {
            moveNode(node, convxs);
        }
    }

    private static void updateConvxsAndEars(Node<Node<VInd>> node, List<Vec3f> poly,
            CircularLinkList<Node<VInd>> convxs,
            CircularLinkList<Node<VInd>> reflxs,
            CircularLinkList<Node<VInd>> ears) {
        if (node.item.item.list != reflxs) {
            if (isVertexEar(poly, node.item, reflxs)){
                if(node.item.item.list != ears){
                    moveNode(node, ears);
                }
            }else if (node.item.item.list == ears) {
                moveNode(node, convxs);
            }
        }
    }

    private static void moveNode(Node<Node<VInd>> node, CircularLinkList<Node<VInd>> dst) {
        node.item.item.list.remove(node);
        
        dst.add(node.item);
        node.item.item.list = dst;
        node.item.item.node = dst.getLastNode();
    }

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

    private static boolean isVertexConvex(List<Vec3f> poly, Node<VInd> node, Vec3f polyNormal) {
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
            Node<VInd> vertexNode,
            CircularLinkList<Node<VInd>> reflxs) {
        //Triangle vertices ordered
        Vec3f t1 = poly.get(vertexNode.prev.item.index);
        Vec3f t2 = poly.get(vertexNode.item.index);
        Vec3f t3 = poly.get(vertexNode.next.item.index);

        //Check if the triangle contains any projection of the reflex vertices
        Vec3f u = t2.sub_(t1);
        Vec3f v = t3.sub_(t1);
        Vec3f n = u.cross_(v);
        float n2 = n.dot(n);
        for (NodeIterator<Node<VInd>> ite = reflxs.iterator(); ite.hasNext(); ite.move()) {
            Node<VInd> node = ite.getCurrent();
            Vec3f vtx = poly.get(node.item.index);
            if ((vtx != t1) && (vtx != t3)) {
                Vec3f w = vtx.sub_(t1);
                if (isInsideTriangle(u, v, w, n, n2)) {
                    return false;
                }
            }
        }
        return true;
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

}
