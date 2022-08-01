package me.kurva.parser.gui;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    public static void foFrame() {
        JFrame frame = new JFrame("JsonAutoParser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(new Rectangle(50,50,500,500));
        frame.setResizable(false);
        frame.setVisible(true);

        JLabel barik1 = new JLabel("Baritone");
        barik1.setBounds(360,10,200,20);
        barik1.setOpaque(true);
        frame.add(barik1);

        JTextField jsonNameField = new JTextField();
        jsonNameField.setBounds(new Rectangle(100,10,150,25));
        frame.add(jsonNameField);
    }
}
