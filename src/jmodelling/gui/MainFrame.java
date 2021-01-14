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
package jmodelling.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.MeshEditable;
import jmodelling.engine.object.mesh.vertex.Vertex;
import jmodelling.gui.display.DisplayGL;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class MainFrame extends JFrame{

    private JPanel container;
    private DisplayGL displayGL;
    
    public MainFrame(String title){
        super(title);
        
        initComponents();
    }
    
    public void initComponents(){
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));
        
        container = new JPanel();
        //container.setBorder(new LineBorder(Color.black, 1));
        add(container);
        
        GridBagLayout containerLayout = new GridBagLayout();
        container.setLayout(containerLayout);
        
        displayGL = new DisplayGL();
        displayGL.setBorder(new LineBorder(Color.GRAY, 1));
        container.add(displayGL);
        
        GridBagConstraints display3DConstraints = new GridBagConstraints();
        display3DConstraints.weightx = 1.0f;
        display3DConstraints.weighty = 1.0f;
        display3DConstraints.fill = GridBagConstraints.BOTH;
        display3DConstraints.insets = new Insets(5, 5, 5, 5);
        containerLayout.setConstraints(displayGL, display3DConstraints);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        new Vec3f(0, 0, 0).anglesXZDegToVector().print();
        new Vec3f(90, 0, -90).anglesXZDegToVector().print();
        
        long before = System.nanoTime();
        
        MeshEditable meshe = new MeshEditable();
        meshe.addVertex(new Vertex(new Vec3f(0.0f, 0.0f, 0.0f)));
        meshe.addVertex(new Vertex(new Vec3f(1.0f, 0.0f, 0.0f)));
        meshe.addVertex(new Vertex(new Vec3f(0.0f, 1.0f, 0.0f)));
        meshe.addVertex(new Vertex(new Vec3f(1.0f, 1.0f, 0.0f)));
        meshe.addVertex(new Vertex(new Vec3f(0.0f, 0.0f, 0.0f)));

        for(int i = 0; i < 10000; i++){
            meshe.addVertex(new Vertex(new Vec3f()));
        }
        
        meshe.addFace(0, 1, 2);
        
        meshe.addFace(0, 0, 1);
        
        meshe.toMesh();
        
        
        meshe.removeVertices(4);
        meshe.removeVertices(4, 2, 1, 0);
        meshe.removeVertices(0);
        meshe.removeVertices(4);
        meshe.removeVertices(4, 2, 1, 0);
        meshe.removeVertices(0);
        meshe.removeVertices(4);
        meshe.removeVertices(4, 2, 1, 0);
        meshe.removeVertices(0);
        meshe.removeVertices(4);
        meshe.removeVertices(4, 2, 1, 0);
        meshe.removeVertices(0);
        
        System.out.println((System.nanoTime() - before));
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("JNDS").setVisible(true);
            }
        });
    }

}
