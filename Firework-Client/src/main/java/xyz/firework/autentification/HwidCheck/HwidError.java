package xyz.firework.autentification.HwidCheck;

import xyz.firework.autentification.NoStackTraceThrowable;

import javax.swing.*;

public class HwidError {

    public static void Display() {
        xyz.firework.autentification.HwidCheck.HwidError.Frame frame = new xyz.firework.autentification.HwidCheck.HwidError.Frame();
        frame.setVisible(false);
        throw new NoStackTraceThrowable("Verification was unsuccessful!");
    }

    public static class Frame extends JFrame {
        public Frame() {
            this.setTitle("Hwid does not mached!");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            JOptionPane.showMessageDialog(this, "Cry about it :)))", "Hwid does not mached!", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
        }
    }
}