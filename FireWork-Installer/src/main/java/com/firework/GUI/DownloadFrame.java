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
        JFrame frame = new JFrame("Uranium Installer");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        //Centering window
        frame.setBounds(x,y,450,330);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setBackground(Color.DARK_GRAY);

        JLabel label = new JLabel("Uranium Installer");
        label.setBounds(450/2 - 200/2,3,200,30);
        label.setOpaque(true);
        label.setForeground(Color.white);
        label.setBackground(Color.DARK_GRAY);
        label.setFont(new Font("Vernanda",Font.BOLD,24));


        JCheckBox barik = new JCheckBox();
        barik.setBounds(410,195,40,40);
        barik.setBackground(Color.DARK_GRAY);
        barik.setForeground(Color.GRAY);

        JProgressBar bar = new JProgressBar();
        bar.setBounds(10,270,420,25);
        bar.setStringPainted(true);
        bar.setMinimum(0);
        bar.setMaximum(100);
        frame.add(bar);

        JLabel barik1 = new JLabel("Baritone");
        barik1.setBounds(360,205,200,20);
        barik1.setOpaque(true);
        barik1.setForeground(Color.white);
        barik1.setBackground(Color.DARK_GRAY);

        JButton button = new JButton("Download");
        button.setBounds(450/2 - 430/2,235,420,25);
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
        frame.add(label);
        frame.add(button);
        frame.add(barik);
        frame.add(barik1);



        JTextField text = new JTextField("s1ash#4203");
        text.setBounds(110,205,140,20);
        frame.add(text);
        text.setForeground(Color.GRAY);

        JLabel texttotext = new JLabel("Link ur discord:");
        texttotext.setBounds(10,205,200,20);
        texttotext.setForeground(Color.white);
        texttotext.setBackground(Color.DARK_GRAY);
        frame.add(texttotext);

        JButton btn = new JButton("Link");
        btn.setBounds(260,205,80,20);
        btn.setBackground(Color.GRAY);
        btn.setForeground(Color.white);
        frame.add(btn);
    }
}