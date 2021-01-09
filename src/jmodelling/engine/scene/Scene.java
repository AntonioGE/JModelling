/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.scene;

import java.util.HashMap;
import jmodelling.engine.object.Object3D;
import jmodelling.engine.object.camera.CamArcball;

/**
 *
 * @author ANTONIO
 */
public class Scene {
    
    public HashMap<String, Object3D> objects;
    
    public Scene(){
        objects = new HashMap<>();
    }
    
}
