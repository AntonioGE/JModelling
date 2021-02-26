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
import jmodelling.utils.collections.CircularLinkedListOld;

/**
 *
 * @author ANTONIO
 */
public class Triangulator {

    public static void earClipping(List<Vec3f> poly) {
        Vec3f normal = getPolygonNormal(poly);

        normal.print();

        LinkedList<Integer> vtxs = new LinkedList<>();
        LinkedList<Integer> convx = new LinkedList<>();
        LinkedList<Integer> reflx = new LinkedList<>();
        LinkedList<Integer> ears = new LinkedList<>();

        for (int i = 0; i < poly.size(); i++) {
            Vec3f vPrev = poly.get(i);
            Vec3f vCurr = poly.get((i + 1) % poly.size());
            Vec3f vNext = poly.get((i + 2) % poly.size());

            Vec3f triNormal = vCurr.sub_(vPrev).cross(vNext.sub_(vCurr)).normalize();
            if (triNormal.dot(normal) > 0.0f) {
                convx.add((i + 1) % poly.size());
            } else {
                reflx.add((i + 1) % poly.size());
            }

            vtxs.add(i);
        }

        /*
        ListIterator<Integer> iteConvx = convx.listIterator();
        while (iteConvx.hasNext()) {

            int idxCurr = iteConvx.next();
            Vec3f vCurr = poly.get(idxCurr);

            //Vec3f u = poly.get(idxCurr + 1).sub_(poly.get(idxCurr + 1))
            ListIterator<Integer> iteReflx = reflx.listIterator();
            while (iteReflx.hasNext()) {

            }

        }
         */
        System.out.println("Done");
    }

    public static void earClipping2(List<Vec3f> poly) {
        Vec3f normal = getPolygonNormal(poly);

        normal.print();

        CircularLinkedList<Vec3f> vtxs = new CircularLinkedList<>();
        LinkedList<Vec3f> convx = new LinkedList<>();
        LinkedList<Vec3f> reflx = new LinkedList<>();
        LinkedList<Vec3f> ears = new LinkedList<>();
        
        poly.forEach((vtx) -> {
            vtxs.add(vtx);
        });
        
        for(CircularLinkedList<Vec3f>.CircularIterator<Vec3f> iteVtxs = vtxs.getIterator(); 
                iteVtxs.hasNext(); iteVtxs.move()){
            Vec3f vPrev = iteVtxs.getPrev();
            Vec3f vCurr = iteVtxs.getCurrent();
            Vec3f vNext = iteVtxs.getNext();
            
            Vec3f triNormal = vCurr.sub_(vPrev).cross(vNext.sub_(vCurr)).normalize();
            if (triNormal.dot(normal) > 0.0f) {
                convx.add(vCurr);
            } else {
                reflx.add(vCurr);
            }
        }
    }

    /*
    public static void earClipping(List<Vec3f> poly) {
        Vec3f normal = getPolygonNormal(poly);

        normal.print();

        LinkedList<Vec3f> vtxs = new LinkedList<>();
        LinkedList<Vec3f> convx = new LinkedList<>();
        LinkedList<Vec3f> reflx = new LinkedList<>();
        LinkedList<Vec3f> ears = new LinkedList<>();

        poly.forEach((vtx) -> {
            vtxs.add(vtx);
        });

        ListIterator<Vec3f> vtxsIte = vtxs.listIterator();
        while (vtxsIte.hasNext()) {
            Vec3f vPrev = vtxsIte.previous();
            Vec3f vCurr = vtxsIte.next();
            Vec3f vNext = vtxsIte.next();
            vtxsIte.previous();

            vtxs.Vec3f triNormal = vCurr.sub_(vPrev).cross(vNext.sub_(vCurr)).normalize();
            if (triNormal.dot(normal) > 0.0f) {
                convx.add(vCurr);
            } else {
                reflx.add(vCurr);
            }
        }

        ListIterator<Vec3f> iteConvx = convx.listIterator();
        while (iteConvx.hasNext()) {

            Vec3f vCurr = poly.get(idxCurr);

            //Vec3f u = poly.get(idxCurr + 1).sub_(poly.get(idxCurr + 1))
            ListIterator<Integer> iteReflx = reflx.listIterator();
            while (iteReflx.hasNext()) {

            }

        }

        System.out.println("Done");
    }*/
    public static void earClipping(float[] vtxArray, int[] vInds) {

        List<Vec3f> vtxs = new ArrayList<>();
        for (Integer vInd : vInds) {
            vtxs.add(new Vec3f(vtxArray, vInd * 3));
        }

        Vec3f normal = getPolygonNormal(vtxs);

        List<Integer> convx = new LinkedList<>();
        List<Integer> reflx = new LinkedList<>();
        List<Integer> ears = new LinkedList<>();

        for (int i = 0; i < vtxs.size(); i++) {
            Vec3f vPrev = vtxs.get(i);
            Vec3f vCurr = vtxs.get((i + 1) % vtxs.size());
            Vec3f vNext = vtxs.get((i + 2) % vtxs.size());

            float dot = vPrev.sub_(vCurr).dot(vNext.sub_(vNext));
            //float det = 
            //if(dot >)
            //(b.x - a.x) * (c.y - b.y) - (c.x - b.x) * (b.y - a.y) > 0
        }

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

}
