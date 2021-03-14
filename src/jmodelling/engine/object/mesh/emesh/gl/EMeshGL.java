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
package jmodelling.engine.object.mesh.emesh.gl;

import com.jogamp.opengl.GL2;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.emesh.EMesh;
import jmodelling.engine.object.mesh.emesh.Edge;
import jmodelling.engine.object.mesh.emesh.Loop;
import jmodelling.engine.object.mesh.emesh.Polygon;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.CollectionUtils;

/**
 *
 * @author ANTONIO
 */
public class EMeshGL implements ElementGL {

    public HashMap<Material, EShapeGL> shapes;
    public EWireGL wireframe;
    public EPointsGL points;

    public EMeshGL(EMesh emesh) {
        genData(emesh);
    }

    public final void genData(EMesh emesh) {
        points = genPoints(emesh);

        wireframe = genWireframe(emesh);

        shapes = genShapes(emesh);
    }
    
    public final void updateData(EMesh emesh){
        updateShapes(emesh, shapes);
        updateWireframe(emesh, wireframe);
        updatePoints(emesh, points);
        //updateShapesVtxs(emesh, shapes);
    }

    public static final HashMap<Material, EShapeGL> genShapes(EMesh emesh) {
        HashMap<Material, EShapeGL> newShapes = CollectionUtils.newHashMap(emesh.polyGroups.size());
        for (Map.Entry<Material, LinkedHashSet<Polygon>> entry : emesh.polyGroups.entrySet()) {
            EShapeGL shapeGL = new EShapeGL(entry.getKey(), emesh.getNumTris(entry.getKey()));
            shapeGL.vtxs.mark();
            shapeGL.nrms.mark();
            shapeGL.clrs.mark();
            shapeGL.uvs.mark();
            for (Polygon poly : entry.getValue()) {
                for (Loop loop : poly.tris) {
                    loop.vtx.writeInBuffer(shapeGL.vtxs);
                    loop.nrm.writeInBuffer(shapeGL.nrms);
                    //loop.clr.writeInBuffer(shapeGL.clrs);
                    loop.uv.writeInBuffer(shapeGL.uvs);
                }
                if(emesh.isPolygonSelected(poly)){
                    for (Loop loop : poly.tris) {
                        shapeGL.clrs.put(1.0f);
                        shapeGL.clrs.put(0.8f);
                        shapeGL.clrs.put(0.5f);
                    }
                }else{
                    for (Loop loop : poly.tris) {
                        shapeGL.clrs.put(1.0f);
                        shapeGL.clrs.put(1.0f);
                        shapeGL.clrs.put(1.0f);
                    }
                }
            }
            shapeGL.vtxs.reset();
            shapeGL.nrms.reset();
            shapeGL.clrs.reset();
            shapeGL.uvs.reset();

            newShapes.put(entry.getKey(), shapeGL);
        }
        return newShapes;
    }
    
    public static final HashMap<Material, EShapeGL> genShapes2(EMesh emesh){
        HashMap<Material, EShapeGL> newShapes = CollectionUtils.newHashMap(emesh.polyGroups.size());
        for (Map.Entry<Material, LinkedHashSet<Polygon>> entry : emesh.polyGroups.entrySet()) {
            EShapeGL shapeGL = new EShapeGL(entry.getKey(), emesh.getNumTris(entry.getKey()));
            newShapes.put(entry.getKey(), shapeGL);
        }
        return newShapes;
    }
   
    
    public static final void updateShapes(EMesh emesh, HashMap<Material, EShapeGL> shapes){
        for (Map.Entry<Material, LinkedHashSet<Polygon>> entry : emesh.polyGroups.entrySet()) {
            EShapeGL shapeGL = shapes.get(entry.getKey());
            shapeGL.vtxs.mark();
            shapeGL.nrms.mark();
            shapeGL.clrs.mark();
            shapeGL.uvs.mark();
            for (Polygon poly : entry.getValue()) {
                for (Loop loop : poly.tris) {
                    loop.vtx.writeInBuffer(shapeGL.vtxs);
                    loop.nrm.writeInBuffer(shapeGL.nrms);
                    //loop.clr.writeInBuffer(shapeGL.clrs);
                    loop.uv.writeInBuffer(shapeGL.uvs);
                }
                
                if(emesh.isPolygonSelected(poly)){
                    for (Loop loop : poly.tris) {
                        shapeGL.clrs.put(1.0f);
                        shapeGL.clrs.put(0.8f);
                        shapeGL.clrs.put(0.5f);
                    }
                }else{
                    for (Loop loop : poly.tris) {
                        shapeGL.clrs.put(1.0f);
                        shapeGL.clrs.put(1.0f);
                        shapeGL.clrs.put(1.0f);
                    }
                }
            }
            shapeGL.vtxs.reset();
            shapeGL.nrms.reset();
            shapeGL.clrs.reset();
            shapeGL.uvs.reset();
            
            shapeGL.updateVtxs();
            shapeGL.updateNrms();
            shapeGL.updateClrs();
            shapeGL.updateUvs();
        }
    }
    
    public static final void updateShapesVtxs(EMesh emesh, HashMap<Material, EShapeGL> shapes){
        for (Map.Entry<Material, LinkedHashSet<Polygon>> entry : emesh.polyGroups.entrySet()) {
            EShapeGL shapeGL = shapes.get(entry.getKey());
            shapeGL.vtxs.mark();
            for (Polygon poly : entry.getValue()) {
                for (Loop loop : poly.tris) {
                    loop.vtx.writeInBuffer(shapeGL.vtxs);
                }
            }
            shapeGL.vtxs.reset();
            shapeGL.updateVtxs();
        }
    }

    public static final EWireGL genWireframe(EMesh emesh) {
        EWireGL wireframe = new EWireGL(emesh.edges.size());

        wireframe.vtxs.mark();
        wireframe.clrs.mark();
        for (Edge edge : emesh.edges.values()) {
            edge.v0.writeInBuffer(wireframe.vtxs);
            edge.v1.writeInBuffer(wireframe.vtxs);
        }
        
        for(Edge edge : emesh.edges.values()){
            if(emesh.selectedVtxs.contains(edge.v0)){
                wireframe.clrs.put(1.0f);
                wireframe.clrs.put(0.62f);
                wireframe.clrs.put(0.0f);
            }else{
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
            }
            if(emesh.selectedVtxs.contains(edge.v1)){
                wireframe.clrs.put(1.0f);
                wireframe.clrs.put(0.62f);
                wireframe.clrs.put(0.0f);
            }else{
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
            }
        }
        /*
        for (int i = 0, size = emesh.edges.size() * 3 * 2; i < size; i++) {
            wireframe.clrs.put(0.0f);
        }*/
        wireframe.vtxs.reset();
        wireframe.clrs.reset();
        return wireframe;
    }

    public static final void updateWireframe(EMesh emesh, EWireGL wireframe){
        wireframe.vtxs.mark();
        wireframe.clrs.mark();
        for (Edge edge : emesh.edges.values()) {
            edge.v0.writeInBuffer(wireframe.vtxs);
            edge.v1.writeInBuffer(wireframe.vtxs);
        }
        
        for(Edge edge : emesh.edges.values()){
            if(emesh.selectedVtxs.contains(edge.v0)){
                wireframe.clrs.put(1.0f);
                wireframe.clrs.put(0.62f);
                wireframe.clrs.put(0.0f);
            }else{
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
            }
            if(emesh.selectedVtxs.contains(edge.v1)){
                wireframe.clrs.put(1.0f);
                wireframe.clrs.put(0.62f);
                wireframe.clrs.put(0.0f);
            }else{
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
                wireframe.clrs.put(0.0f);
            }
        }
        /*
        for (int i = 0, size = emesh.edges.size() * 3 * 2; i < size; i++) {
            wireframe.clrs.put(0.0f);
        }*/
        wireframe.vtxs.reset();
        wireframe.clrs.reset();
    }
    
    public static final EPointsGL genPoints(EMesh emesh) {
        EPointsGL points = new EPointsGL(emesh.vtxs.size());
        points.vtxs.mark();
        points.clrs.mark();
        for (Vec3f vtx : emesh.vtxs) {
            vtx.writeInBuffer(points.vtxs);
        }
        for (Vec3f vtx : emesh.vtxs) {
            if(emesh.selectedVtxs.contains(vtx)){
                points.clrs.put(1.0f);
                points.clrs.put(0.5f);
                points.clrs.put(0.0f);
            }else{
                points.clrs.put(0.0f);
                points.clrs.put(0.0f);
                points.clrs.put(0.0f);
            }
        }
        /*
        for (int i = 0, size = emesh.vtxs.size() * 3; i < size; i++) {
            points.clrs.put(0.0f);
        }*/
        points.vtxs.reset();
        points.clrs.reset();
        return points;
    }
    
    public static final void updatePoints(EMesh emesh, EPointsGL points){
        points.vtxs.mark();
        points.clrs.mark();
        for (Vec3f vtx : emesh.vtxs) {
            vtx.writeInBuffer(points.vtxs);
        }
        for (Vec3f vtx : emesh.vtxs) {
            if(emesh.selectedVtxs.contains(vtx)){
                points.clrs.put(1.0f);
                points.clrs.put(0.5f);
                points.clrs.put(0.0f);
            }else{
                points.clrs.put(0.0f);
                points.clrs.put(0.0f);
                points.clrs.put(0.0f);
            }
        }
        points.vtxs.reset();
        points.clrs.reset();
    }
    
    @Override
    public void init(GL2 gl) {
        for (EShapeGL shape : shapes.values()) {
            shape.init(gl);
        }
        wireframe.init(gl);
        points.init(gl);
    }

    @Override
    public void render(GL2 gl) {
        //gl.glDisable(GL2.GL_LIGHTING);
        //gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_DIFFUSE);
        //gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glDepthRange(0.002f, 1.0f);//Use this for Perspective camera
        //gl.glDepthRange(0.00001f, 1.0f);//Use this for Ortho camera
        for (EShapeGL shape : shapes.values()) {
            shape.render(gl);
        }
        gl.glDepthRange(0.0f, 1.0f);
        
        gl.glDepthMask(false);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glPointSize(3);
        wireframe.render(gl);
        points.render(gl);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDepthMask(true);
    }

    @Override
    public void update(GL2 gl) {
        for (EShapeGL shape : shapes.values()) {
            shape.update(gl);
        }
        wireframe.update(gl);
        points.update(gl);
    }

    @Override
    public void delete(GL2 gl) {
        for (EShapeGL shape : shapes.values()) {
            shape.delete(gl);
        }
        wireframe.delete(gl);
        points.delete(gl);
    }

}
