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
import java.util.Scanner;
import jmodelling.engine.object.newmesh.Mesh;

/**
 *
 * @author ANTONIO
 */
public class ObjReader {

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
        public ArrayList<Integer[]> vInds;
        public ArrayList<Integer[]> tInds;
        public ArrayList<Integer[]> nInds;

        public Shape(String matName) {
            this.matName = matName;
            vInds = new ArrayList<>();
            tInds = new ArrayList<>();
            nInds = new ArrayList<>();
        }
    };

    public static Mesh readObj(String path) throws IOException {

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
                        readCoords(sLine, vCoords);
                        break;
                    case "vt":
                        readCoords(sLine, tCoords);
                        break;
                    case "vn":
                        readCoords(sLine, nCoords);
                        break;
                    case "usemtl":
                        currentShape = addShape(sLine, currentObject.shapes);
                        break;
                    case "f":
                        readFace(sLine,
                                currentShape.vInds,
                                currentShape.tInds,
                                currentShape.nInds);
                        break;
                }
            }
        }

        /*
        float[] mesh = new float[fInds.size() * 3 * 3];
        int c = 0;
        for (Integer[] vInds : fInds) {
            for (Integer vInd : vInds) {
                for (Float coord : vCoords.get(vInd)) {
                    mesh[c] = coord;
                    c++;
                }
            }
        }*/
        return null;
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

    private static void readCoords(String[] sLine, ArrayList<Float[]> coords) {
        if (sLine.length != 4) {
            coords.add(null);
        } else {
            try {
                Float[] vertex = new Float[3];
                for (int i = 0; i < vertex.length; i++) {
                    vertex[i] = Float.valueOf(sLine[1 + i]);
                    coords.add(vertex);
                }
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                coords.add(null);
            }
        }
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
