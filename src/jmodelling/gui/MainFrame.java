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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jmodelling.engine.Engine;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.object.mesh.utils.triangulator.EarClipping;
import jmodelling.gui.display.DisplayGL;
import jmodelling.gui.display.EditorDisplayGL;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.collections.IdentitySet;
import jmodelling.utils.collections.node.CircularLinkedHashSet;
import jmodelling.utils.collections.node.CircularLinkedList;
import jmodelling.utils.collections.node.NodeIterator;

/**
 *
 * @author ANTONIO
 */
public class MainFrame extends JFrame {

    private JPanel container;
    private DisplayGL displayGL;
    
    //private EditorDisplayGL editorDisplay1;
    //private EditorDisplayGL editorDisplay2;
    //TODO: move this to engine?
    public List<EditorDisplayGL> editorDisplays;
    
    private Engine engine;

    public MainFrame(String title) {
        super(title);

        engine = new Engine(this);
        
        initComponents();
    }

    public final void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));

        container = new JPanel();
        //container.setBorder(new LineBorder(Color.black, 1));
        add(container);

        GridBagLayout containerLayout = new GridBagLayout();
        container.setLayout(containerLayout);

        /*
        displayGL = new DisplayGL();
        displayGL.setBorder(new LineBorder(Color.GRAY, 1));
        container.add(displayGL);*/

        editorDisplays = new ArrayList<>(2);
        EditorDisplayGL editorDisplay1 = new EditorDisplayGL(engine.sharedDrawable, new View3D(engine));
        container.add(editorDisplay1);
        editorDisplays.add(editorDisplay1);
        
        EditorDisplayGL editorDisplay2 = new EditorDisplayGL(engine.sharedDrawable, new View3D(engine));
        container.add(editorDisplay2);
        editorDisplays.add(editorDisplay2);
        
        GridBagConstraints display3DConstraints = new GridBagConstraints();
        display3DConstraints.weightx = 1.0f;
        display3DConstraints.weighty = 1.0f;
        display3DConstraints.fill = GridBagConstraints.BOTH;
        display3DConstraints.insets = new Insets(5, 5, 5, 5);
        //containerLayout.setConstraints(displayGL, display3DConstraints);
        containerLayout.setConstraints(editorDisplay1, display3DConstraints);
        containerLayout.setConstraints(editorDisplay2, display3DConstraints);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArrayList<Vec3f> vtxs = new ArrayList<>();
//        vtxs.add(new Vec3f(-1.0f, -1.0f, 0.0f));
//        vtxs.add(new Vec3f(+1.0f, -1.0f, 0.0f));
//        vtxs.add(new Vec3f(+1.0f, +1.0f, 0.0f));
//        vtxs.add(new Vec3f(+0.4f, -0.4f, 0.0f));
        vtxs.add(new Vec3f(-1.0f, -1.0f, 0.0f));
        vtxs.add(new Vec3f(+1.0f, -3.0f, 0.0f));
        vtxs.add(new Vec3f(+3.0f, -1.0f, 0.0f));
        vtxs.add(new Vec3f(+4.0f, -2.0f, 0.0f));
        vtxs.add(new Vec3f(+6.0f, +1.0f, 0.0f));
        vtxs.add(new Vec3f(+4.0f, +0.5f, 0.0f));
        vtxs.add(new Vec3f(+3.0f, +3.0f, 0.0f));
        vtxs.add(new Vec3f(+2.0f, -1.2f, 0.0f));
        vtxs.add(new Vec3f(+0.0f, -0.6f, 0.0f));
        vtxs.add(new Vec3f(+0.0f, +2.0f, 0.0f));
        EarClipping.triangulate(vtxs);

        CircularLinkedHashSet<String> list = new CircularLinkedHashSet<>();
        list.add("FIRST");
        list.add("SECOND");
        list.add("THIRD");
        list.add("FOURTH");
        list.add("FOURTH");
        list.add("FOURTH");
        list.add("FIFTH");
        
        //list.remove("FIRST");
        
        NodeIterator<String> ite = list.nodeIterator();
        while(ite.hasNext()){
            String s = ite.next();
            System.out.println(s);
            if(s.equals("FIFTH")){
                ite.remove();
            }
        }
        
        for(String s : list){
            System.out.println(s);
        }
        
        System.out.println(Runtime.getRuntime().availableProcessors());

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("JModelling").setVisible(true);
            }
        });
    }

}
