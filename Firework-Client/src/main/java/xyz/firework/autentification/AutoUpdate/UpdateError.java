package xyz.firework.autentification.AutoUpdate;


import xyz.firework.autentification.NoStackTraceThrowable;

import javax.swing.*;

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