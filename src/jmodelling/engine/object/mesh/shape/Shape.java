/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.engine.object.mesh.shape;

import java.nio.FloatBuffer;
import jmodelling.engine.object.material.Material;

/**
 *
 * @author ANTONIO
 */
public class Shape {
    
    public Material material;
    
    public FloatBuffer vCoords;
    public FloatBuffer tCoords;
    public FloatBuffer nCoords;
    public FloatBuffer colors;
}
