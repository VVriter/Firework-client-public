package com.firework.GUI;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/*
@author dazed68
*/

public class DownloadFrame {
    public static void frame(String... args){
        //decorations
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Firework Installer");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        //Centering window
        frame.setBounds(x,y,450,108);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setBackground(Color.DARK_GRAY);



        JCheckBox barik = new JCheckBox();
        barik.setBounds(410,10,20,20);
        barik.setBackground(Color.DARK_GRAY);
        barik.setForeground(Color.GRAY);


        JLabel barik1 = new JLabel("Baritone");
        barik1.setBounds(360,10,200,20);
        barik1.setOpaque(true);
        barik1.setForeground(Color.white);
        barik1.setBackground(Color.DARK_GRAY);

        JButton button = new JButton("Download");
        button.setBounds(450/2 - 430/2,40,420,25);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.white);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (barik.isSelected()){

                }
                if(!barik.isSelected()){

                }
            }
        });

        frame.setVisible(true);
        frame.add(button);
        frame.add(barik);
        frame.add(barik1);



        JTextField text = new JTextField("s1ash#4203");
        text.setBounds(110,10,140,20);
        frame.add(text);
        text.setForeground(Color.GRAY);

        JLabel texttotext = new JLabel("Link ur discord:");
        texttotext.setBounds(10,10,200,20);
        texttotext.setForeground(Color.white);
        texttotext.setBackground(Color.DARK_GRAY);
        frame.add(texttotext);

        JButton btn = new JButton("Link");
        btn.setBounds(260,10,80,20);
        btn.setBackground(Color.GRAY);
        btn.setForeground(Color.white);
        frame.add(btn);
    }
}