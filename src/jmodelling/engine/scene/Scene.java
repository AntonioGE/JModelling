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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.mesh.MeshObject;
import jmodelling.utils.collections.IdentitySet;

/**
 *
 * @author ANTONIO
 */
public class Scene {

    /**
     * Scene objects
     */
    private final HashMap<String, Object3D> objects;
    private final Map<String, Object3D> objectsReadOnly;

    /**
     * HUD objects
     */
    private final HashMap<String, Object3D> hudObjects;
    private final Map<String, Object3D> hudObjectsReadOnly;

    /**
     * Selection
     */
    private final HashSet<Object3D> selectedObjects;
    private final Set<Object3D> selectedObjectsReadOnly;
    private final HashSet<Object3D> unselectedObjects;
    private final Set<Object3D> unselectedObjectsReadOnly;
    private Object3D lastSelectedObject;

    /**
     * Objects GL updating
     */
    private final IdentitySet<Object3D> objectsToInit;
    private final IdentitySet<Object3D> objectsToDelete;
    private final IdentitySet<Object3D> objectsToUpdate;

    public Scene() {
        objects = new HashMap<>();
        objectsReadOnly = Collections.unmodifiableMap(objects);

        hudObjects = new HashMap<>();
        hudObjectsReadOnly = Collections.unmodifiableMap(hudObjects);

        selectedObjects = new HashSet<>();
        selectedObjectsReadOnly = Collections.unmodifiableSet(selectedObjects);
        unselectedObjects = new HashSet<>();
        unselectedObjectsReadOnly = Collections.unmodifiableSet(unselectedObjects);

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

    public boolean addObject(Object3D object) {
        if (objects.containsKey(object.name)) {
            return false;
        }
        objects.put(object.name, object);
        objectsToInit.add(object);
        unselectedObjects.add(object);
        return true;
    }

    public boolean removeObject(Object3D object) {
        if (!objects.containsKey(object.name)) {
            return false;
        }

        deselectObject(object);
        objects.remove(object.name);
        objectsToDelete.add(object);
        return true;
    }

    public boolean update(Object3D object) {
        objectsToUpdate.add(object);
        return true;
    }

    public Collection<Object3D> getObjects() {
        return objectsReadOnly.values();
    }

    public Set<MeshObject> getMeshObjects() {
        HashSet<MeshObject> meshObjects = new HashSet<>();
        objects.values().stream().filter((obj) -> (obj.getType().equals("MESH"))).forEachOrdered((obj) -> {
            try {
                meshObjects.add((MeshObject) obj);
            } catch (ClassCastException ex) {

            }
        });
        return meshObjects;
    }

    public Set<Object3D> getSelectedObjects() {
        return selectedObjectsReadOnly;
    }

    public Set<Object3D> getUnselectedObjects() {
        return unselectedObjectsReadOnly;
    }

    public boolean selectObject(Object3D object) {
        if (!objects.containsKey(object.name)) {
            return false;
        }
        selectedObjects.add(object);
        unselectedObjects.remove(object);
        lastSelectedObject = object;
        return true;
    }

    public boolean deselectObject(Object3D object) {
        if (!selectedObjects.contains(object)) {
            return false;
        }
        selectedObjects.remove(object);
        unselectedObjects.add(object);
        lastSelectedObject = null;
        return true;
    }

    public boolean selectAll() {
        if (objects.isEmpty()) {
            return false;
        } else {
            objects.values().forEach((obj) -> {
                selectedObjects.add(obj);
            });
            unselectedObjects.clear();
            return true;
        }
    }

    public boolean deselectAll() {
        if (objects.isEmpty()) {
            return false;
        } else {
            objects.values().forEach((obj) -> {
                unselectedObjects.add(obj);
            });
            selectedObjects.clear();
            lastSelectedObject = null;
            return true;
        }
    }

    public boolean selectOnlyObject(Object3D obj) {
        if (!objects.containsKey(obj.name)) {
            return false;
        } else {
            deselectAll();
            selectObject(obj);
            return true;
        }
    }

    public boolean isAnyObjectSelected() {
        return !selectedObjects.isEmpty();
    }

    public boolean areAllObjectsSelected() {
        if (objects.isEmpty()) {
            return false;
        } else {
            return objects.size() == selectedObjects.size();
        }
    }
    
    public boolean areAllObjectsUnselected(){
        if (objects.isEmpty()) {
            return false;
        } else {
            return objects.size() == unselectedObjects.size();
        }
    }

    public Object3D getLastSelectedObject() {
        return lastSelectedObject;
    }

    public boolean isLastObjectSelected() {
        return lastSelectedObject != null;
    }

    public boolean addHudObject(Object3D object) {
        if (hudObjects.containsKey(object.name)) {
            return false;
        }
        hudObjects.put(object.name, object);
        objectsToInit.add(object);
        return true;
    }

    public boolean replaceHudObject(Object3D object) {
        if (hudObjects.containsKey(object.name)) {
            removeHudObject(object);
        }
        return addHudObject(object);
    }

    public boolean removeHudObject(Object3D object) {
        if (object == null || !hudObjects.containsKey(object.name)) {
            return false;
        }

        hudObjects.remove(object.name);
        objectsToDelete.add(object);
        return true;
    }

    public boolean removeHudObject(String name) {
        return removeHudObject(hudObjects.get(name));
    }

    public Collection<Object3D> getHudObjects() {
        return hudObjectsReadOnly.values();
    }

}
