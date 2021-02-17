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
import java.awt.Point;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import jmodelling.engine.formats.obj.ObjReader;
import jmodelling.engine.object.material.Material;
import jmodelling.engine.object.mesh.MeshEditable;
import jmodelling.engine.object.newmesh.Vertex;
import jmodelling.engine.object.newmesh.Edge;
import jmodelling.gui.display.DisplayGL;
import jmodelling.math.mat.Mat3f;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.ListUtils;
import jmodelling.utils.collections.IdentitySet;

/**
 *
 * @author ANTONIO
 */
public class MainFrame extends JFrame {

    private JPanel container;
    private DisplayGL displayGL;

    public MainFrame(String title) {
        super(title);

        initComponents();
    }

    public void initComponents() {
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

        System.out.println((System.nanoTime() - before));

        System.out.println(Runtime.getRuntime().availableProcessors());

        Vertex v1 = new Vertex(0.0f, 0.0f, 0.0f);
        Vertex v2 = new Vertex(1.0f, 1.0f, 1.0f);
        Vertex v3 = new Vertex(1.0f, 1.0f, 1.0f);
        Edge e1 = new Edge(v1, v2);
        Edge e2 = new Edge(v1, v2);
        System.out.println(e1.equals(e2));

        Point s1 = new Point(0, 0);
        Point s2 = new Point(0, 0);
        System.out.println("Duplicate: " + ListUtils.hasDuplicates(s1, s2));

        IdentitySet set = new IdentitySet();
        Point p = new Point(0, 0);
        set.add(new Point(0, 0));
        set.add(new Point(0, 0));
        set.add(p);
        set.add(p);
        System.out.println();
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("JNDS").setVisible(true);
            }
        });
    }

}
