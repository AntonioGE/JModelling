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
package jmodelling.engine.formats.obj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.emesh.EMesh;
import jmodelling.math.vec.Vec2f;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class ObjReaderOld {

    private static class Object3D {

        public String name;
        HashMap<String, Shape> shapes;

        public Object3D(String name) {
            this.name = name;
            shapes = new HashMap();
            shapes.put("", new Shape(""));
        }
    };

    private static class Shape {

        public String matName;
        public ArrayList<Face> faces;

        public Shape(String matName) {
            this.matName = matName;
            faces = new ArrayList<>();
        }
    };

    private static class Face {

        final ArrayList<VertexIndx> vtxs;

        public Face(ArrayList<VertexIndx> vtxs) {
            this.vtxs = vtxs;
        }

        public Face(int numFaces) {
            vtxs = new ArrayList<>(numFaces);
        }
    }

    private static class VertexIndx {

        public Integer vInd;
        public Integer tInd;
        public Integer nInd;

        public VertexIndx() {

        }

        public VertexIndx(Integer vInd, Integer tInd, Integer nInd) {
            this.vInd = vInd;
            this.tInd = tInd;
            this.nInd = nInd;
        }

    }

    public static HashMap<String, EMesh> readObj(String path) throws IOException {

        HashMap<String, Object3D> objects = new HashMap();

        Object3D defaultObject = new Object3D("");
        Object3D currentObject = defaultObject;
        Shape currentShape = defaultObject.shapes.get("");

        ArrayList<Float[]> vCoords = new ArrayList<>();
        ArrayList<Float[]> tCoords = new ArrayList<>();
        ArrayList<Float[]> nCoords = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(path), "UTF-8")) {
            while (sc.hasNextLine()) {
                String[] sLine = sc.nextLine().split(" ");

                switch (sLine[0]) {
                    case "o":
                        currentObject = addObject(sLine, objects);
                        currentShape = currentObject.shapes.get("");
                        break;
                    case "v":
                        readCoords(sLine, vCoords, 3);
                        break;
                    case "vt":
                        readCoords(sLine, tCoords, 2);
                        break;
                    case "vn":
                        readCoords(sLine, nCoords, 3);
                        break;
                    case "usemtl":
                        currentShape = addShape(sLine, currentObject.shapes);
                        break;
                    case "f":
                        readFace(sLine, currentShape);
                        break;
                }
            }
        }
        
        //Remove the empty default shapes
        for(Object3D o : objects.values()){
            if(o.shapes.get("").faces.isEmpty()){
                o.shapes.remove("");
            }
        }
        
        HashMap<String, EMesh> meshes = new HashMap<>(objects.size());
        for (Object3D o : objects.values()) {
            EMesh m = objectToMesh(o, vCoords, tCoords, nCoords);
            meshes.put(o.name, m);
            System.out.println("Done");
        }

        return meshes;
    }

    private static EMesh objectToMesh(Object3D o, ArrayList<Float[]> vCoords, 
            ArrayList<Float[]> tCoords, ArrayList<Float[]> nCoords){
        EMesh mesh = new EMesh();
        
        HashMap<Integer, Integer> vIndsAdded = new HashMap<>();
        int nextIndex = 0;
        for(Shape shape : o.shapes.values()){
            Material mat = new Material(shape.matName);
            for(Face face : shape.faces){
                List<Integer> vInds = new ArrayList<>(face.vtxs.size());
                List<Vec2f> uvs = new ArrayList<>(face.vtxs.size());
                List<Vec3f> nrms = new ArrayList<>(face.vtxs.size());
                List<Vec3f> clrs = new ArrayList<>(face.vtxs.size());
                for(VertexIndx v : face.vtxs){
                    //Vertex coordinates
                    if(!vIndsAdded.keySet().contains(v.vInd)){
                        Float[] coords = vCoords.get(v.vInd);
                        mesh.addVertex(new Vec3f(coords[0], coords[1], coords[2]));
                        vInds.add(nextIndex);
                        vIndsAdded.put(v.vInd, nextIndex);
                        nextIndex++;
                    }else{
                        vInds.add(vIndsAdded.get(v.vInd));
                    }
                    
                    //Texture coordinates
                    if(v.tInd != null){
                        Float[] coords = tCoords.get(v.tInd);
                        uvs.add(new Vec2f(coords[0], coords[1]));
                    }else{
                        uvs.add(new Vec2f());
                    }
                    
                    //Normals
                    if(v.nInd != null){
                        Float[] coords = nCoords.get(v.nInd);
                        nrms.add(new Vec3f(coords[0], coords[1], coords[2]));
                    }else{
                        nrms.add(new Vec3f());
                    }
                    
                    //Colors
                    clrs.add(new Vec3f(1.0f, 1.0f, 1.0f));
                }
                //mesh.addNewPolygon(mat, vInds, uvs, nrms, clrs);
            }
        }
        return mesh;
    }
    
    private static Shape addShape(String[] sLine, HashMap<String, Shape> shapes) {
        String matName;
        try {
            matName = sLine[1];
        } catch (IndexOutOfBoundsException ex) {
            matName = "";
        }

        if (!shapes.containsKey(matName)) {
            Shape newShape = new Shape(matName);
            shapes.put(newShape.matName, newShape);
            return newShape;
        } else {
            return shapes.get(matName);
        }
    }

    private static Object3D addObject(String[] sLine, HashMap<String, Object3D> objects) {
        String name;
        try {
            name = sLine[1];
        } catch (IndexOutOfBoundsException ex) {
            name = "";
        }

        if (!objects.containsKey(name)) {
            Object3D newObject = new Object3D(name);
            objects.put(newObject.name, newObject);
            return newObject;
        } else {
            return objects.get(name);
        }
    }

    private static void readCoords(String[] sLine, ArrayList<Float[]> coords, int nCoords) {
        if (sLine.length != nCoords + 1) {
            coords.add(null);
        } else {
            try {
                Float[] vertex = new Float[nCoords];
                for (int i = 0; i < vertex.length; i++) {
                    vertex[i] = Float.valueOf(sLine[1 + i]);
                }
                coords.add(vertex);
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                coords.add(null);
            }
        }
    }

    private static void readFace(String[] sLine, Shape shape) {
        if (sLine.length < 4) {
            return;
        }
        final int indices = sLine.length - 1;
        Face face = new Face(indices);

        boolean missingTInd = false;
        boolean missingNInd = false;
        for (int i = 0; i < indices; i++) {
            String[] sFace = sLine[i + 1].split("/");
            if (sFace.length < 1 || sFace.length > 3) {
                return;
            }

            VertexIndx v = new VertexIndx();
            try {
                v.vInd = Integer.valueOf(sFace[0]) - 1;
            } catch (NumberFormatException ex) {
                return;
            }

            try {
                v.tInd = Integer.valueOf(sFace[1]) - 1;
            } catch (NumberFormatException ex) {
                missingTInd = true;
            }

            try {
                v.nInd = Integer.valueOf(sFace[2]) - 1;
            } catch (NumberFormatException ex) {
                missingNInd = true;
            }

            face.vtxs.add(v);
        }

        if (missingTInd) {
            face.vtxs.forEach((v) -> {
                v.tInd = null;
            });
        }
        if (missingNInd) {
            face.vtxs.forEach((v) -> {
                v.nInd = null;
            });
        }

        shape.faces.add(face);
    }

    private static void readFace(String[] sLine, ArrayList<Integer[]> vInds,
            ArrayList<Integer[]> tInds, ArrayList<Integer[]> nInds) {
        if (sLine.length < 4) {
            vInds.add(null);
            tInds.add(null);
            nInds.add(null);
        } else {
            try {
                final int indices = sLine.length - 1;
                Integer[] vInd = new Integer[indices];
                Integer[] tInd = new Integer[indices];
                Integer[] nInd = new Integer[indices];
                for (int i = 0; i < indices; i++) {
                    String[] sFace = sLine[i + 1].split("/");
                    vInd[i] = Integer.valueOf(sFace[0]) - 1;
                    if (tInd != null) {
                        try {
                            tInd[i] = Integer.valueOf(sFace[1]) - 1;
                        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                            tInd = null;
                        }
                    }
                    if (nInd != null) {
                        try {
                            nInd[i] = Integer.valueOf(sFace[2]) - 1;
                        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                            nInd = null;
                        }
                    }
                }
                vInds.add(vInd);
                tInds.add(tInd);
                nInds.add(nInd);
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                vInds.add(null);
                tInds.add(null);
                nInds.add(null);
            }
        }
    }

}
