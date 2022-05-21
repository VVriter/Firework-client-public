package xyz.firework.autentification.AutoUpdate;


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class UpdateError {

    public static void Display() {
        Frame frame = new Frame();
        frame.setVisible(false);
        throw new NoStackTraceThrowable("Verification was unsuccessful!");
    }

    public static class Frame extends JFrame {
        public Frame() {
            this.setTitle("Update!");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
                       JOptionPane.showMessageDialog(this, "Open installer & update ur client, reload minecraft", "Update!", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
        }
    }
}