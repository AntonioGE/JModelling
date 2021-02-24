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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class Triangulator {

    public static void earClipping(float[] vtxArray, int[] vInds) {
        List<Integer> convx = new LinkedList<>();
        List<Integer> reflx = new LinkedList<>();
        List<Integer> ears = new LinkedList<>();

        List<Vec3f> vtxs = new ArrayList<>();
        for (Integer vInd : vInds) {
            vtxs.add(new Vec3f(vtxArray, vInd * 3));
        }

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
}
