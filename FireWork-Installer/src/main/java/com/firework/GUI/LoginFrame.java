package com.firework.GUI;

import com.firework.Utilites.LoginCheck.LoginManager;
import com.firework.Utilites.Pastebin.exceptions.PasteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame {


    public static void frame(String... args)  {


        JFrame.setDefaultLookAndFeelDecorated(true);


        //Window Settings
        JFrame frame = new JFrame("Firework Client Installer");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        //Centering window
        frame.setBounds(x,y,350,140);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setBackground(Color.DARK_GRAY);




        //login
        JLabel login = new JLabel("Login:");
        login.setBounds(25,10,50,20);
        login.setOpaque(true);
        login.setForeground(Color.white);
        login.setBackground(Color.DARK_GRAY);
        login.setFont(new Font("Vernanda",Font.BOLD,15));

        JTextField pass3 = new JTextField();
        pass3.setBounds(100,10,230,20);



        //password
        JLabel pass = new JLabel("Password:");
        pass.setBounds(12,40,80,20);
        pass.setOpaque(true);
        pass.setForeground(Color.white);
        pass.setBackground(Color.DARK_GRAY);
        pass.setFont(new Font("Vernanda",Font.BOLD,15));

        JPasswordField pass2 = new JPasswordField();
        pass2.setBounds(100,40,230,20);



        //button
        JButton button = new JButton("Login");
        button.setBounds(10,70,322,25);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.white);

        button.addActionListener(new ActionListener() {
            @Override
            public  void actionPerformed(ActionEvent e) {
                LoginManager.whatcheck = pass3.getText()+":"+pass2.getText();

                    LoginManager.loginCheck();

            }
        });


        //add
        frame.setVisible(true);
        frame.add(login);
        frame.add(pass);
        frame.add(button);
        frame.add(pass2);
        frame.add(pass3);
    }
}