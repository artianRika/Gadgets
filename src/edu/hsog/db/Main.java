package edu.hsog.db;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame jFrame = generateJFrame();
//        UIManager.setLookAndFeel("java.swing.plaf.nimbus.NimbusLookAndFeel");

        jFrame.setVisible(true);
//        Globals.initConnectionPool();
    }

    public static JFrame generateJFrame(){
        JFrame jFrame = new JFrame();

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 600);


        GUI gui = new GUI();
        jFrame.getContentPane().add(gui.getMasterPanel());

        gui.getMasterPanel().setPreferredSize(new Dimension(800, 600));
        return jFrame;
    }
}