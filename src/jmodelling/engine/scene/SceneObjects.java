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
package jmodelling.engine.scene;

import com.jogamp.opengl.GL2;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jmodelling.engine.object.Object3D;
import jmodelling.utils.collections.IdentitySet;

/**
 *
 * @author ANTONIO
 */
public class SceneObjects {
    
    public final HashSet<Object3D> objects;
    public final Set<Object3D> objectsReadOnly;

    public final HashSet<Object3D> selectedObjects;
    public Object3D lastSelectedObject;

    public final IdentitySet<Object3D> objectsToInit;
    public final IdentitySet<Object3D> objectsToDelete;
    public final IdentitySet<Object3D> objectsToUpdate;
    
    public SceneObjects(){
        objects = new HashSet<>();
        objectsReadOnly = Collections.unmodifiableSet(objects);

        selectedObjects = new HashSet<>();

        objectsToInit = new IdentitySet();
        objectsToDelete = new IdentitySet();
        objectsToUpdate = new IdentitySet();
    }
    
    public void updateGL(GL2 gl) {
        removeObjects(gl);
        updateObjects(gl);
        initObjects(gl);
    }

    private void removeObjects(GL2 gl) {
        objectsToDelete.forEach((obj) -> {
            obj.delete(gl);
        });
        objectsToDelete.clear();
    }

    private void initObjects(GL2 gl) {
        objectsToInit.forEach((obj) -> {
            obj.init(gl);
            System.out.println("Init: " + obj.name);
        });
        objectsToInit.clear();
    }

    private void updateObjects(GL2 gl) {
        objectsToUpdate.forEach((obj) -> {
            obj.update(gl);
        });
        objectsToUpdate.clear();
    }

    public boolean add(Object3D object) {
        if (objects.contains(object)) {
            return false;
        }
        objects.add(object);
        objectsToInit.add(object);
        return true;
    }

    public boolean remove(Object3D object) {
        if (!objects.contains(object)) {
            return false;
        }

        //deselectObject(object);
        objects.remove(object);
        objectsToDelete.add(object);
        return true;
    }

    public boolean update(Object3D object) {
        objectsToUpdate.add(object);
        return true;
    }

    public Set<Object3D> getObjects() {
        return objectsReadOnly;
    }
    
}
