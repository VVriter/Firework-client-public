package com.firework.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.event.MouseListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;


public class SystemTray {
    public static void sysTray(){
        final TrayIcon trayIcon;
        if (java.awt.SystemTray.isSupported()) {

            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            Image image = null;
            try {
                InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("firework/textures/icon32.png")).getInputStream();
                image = ImageIO.read(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            MouseListener mouseListener = new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse clicked!");
                }

                public void mouseEntered(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse entered!");
                }

                public void mouseExited(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse exited!");
                }

                public void mousePressed(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse pressed!");
                }

                public void mouseReleased(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse released!");
                }
            };


            trayIcon = new TrayIcon(image, "Firework Client");




            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(mouseListener);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }

        } else {

            //  System Tray is not supported

        }
    }
}
