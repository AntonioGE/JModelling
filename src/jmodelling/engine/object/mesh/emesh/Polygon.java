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
package jmodelling.engine.object.mesh.emesh;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.utils.triangulator.EarClipping;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.CollectionUtils;
import jmodelling.utils.collections.node.CircularLinkedHashSet;
import jmodelling.utils.collections.node.NodeIterator;

/**
 *
 * @author ANTONIO
 */
public class Polygon {

    public CircularLinkedHashSet<Loop> loops;
    public Material mat;

    public List<Loop> tris;

    public Polygon(CircularLinkedHashSet<Loop> loops, Material mat) {
        if(loops.size() < 3){
            throw new IllegalArgumentException();
        }
        this.loops = loops;
        this.mat = mat;
        
        updateTris();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for(Loop loop : loops){
            hash ^= System.identityHashCode(loop.vtx);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Polygon other = (Polygon) obj;
        if(this.loops.size() == other.loops.size()){
            return this.loops.containsAll(other.loops.getSet());
        }
        return false;
    }
    
    public Vec3f getNormal() {
        Vec3f normal = new Vec3f();

        for (NodeIterator<Loop> ite = loops.nodeIterator(); ite.hasNext();) {
            Vec3f c = ite.next().vtx;
            Vec3f n = ite.getNextNode().item().vtx;
            normal.x += (c.y - n.y) * (c.z + n.z);
            normal.y += (c.z - n.z) * (c.x + n.x);
            normal.z += (c.x - n.x) * (c.y + n.y);
        }
        return normal.normalize();
    }

    public List<Vec3f> getVertices() {
        List<Vec3f> vtxs = new ArrayList<>(loops.size());
        loops.forEach((loop) -> {
            vtxs.add(loop.vtx);
        });
        return vtxs;
    }

    public boolean isTri() {
        return loops.size() == 3;
    }
    
    public int size(){
        return loops.size();
    }

    public final void updateTris() {
        if (loops.size() == 3) {
            tris = new ArrayList<>(3);
            for(Loop loop : loops){
                tris.add(loop);
            }
        } else if(loops.size() == 4){
            tris = new ArrayList<>(6);
            List<Loop> loopArray = new ArrayList<>(loops.size());
            for(Loop loop : loops){
                loopArray.add(loop);
            }
            tris.add(loopArray.get(0));
            tris.add(loopArray.get(1));
            tris.add(loopArray.get(3));
            tris.add(loopArray.get(1));
            tris.add(loopArray.get(2));
            tris.add(loopArray.get(3));
        } else {
            List<Integer> triInds = EarClipping.triangulate(getVertices());
            tris = new ArrayList<>(triInds.size());
            List<Loop> loopArray = new ArrayList<>(loops.size());
            for(Loop loop : loops){
                loopArray.add(loop);
            }
            for(Integer i : triInds){
                tris.add(loopArray.get(i));
            }
        }
    }
}
