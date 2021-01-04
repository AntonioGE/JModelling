/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import jmodelling.display.DisplayGL;
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
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("JNDS").setVisible(true);
            }
        });
    }

}
