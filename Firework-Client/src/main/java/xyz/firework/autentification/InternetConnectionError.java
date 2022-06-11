package xyz.firework.autentification;

import javax.swing.*;

public class InternetConnectionError {
    public static void Display() {
        xyz.firework.autentification.InternetConnectionError.Frame frame = new xyz.firework.autentification.InternetConnectionError.Frame();
        frame.setVisible(false);
        throw new NoStackTraceThrowable("Internet Connection Error!");
    }

    public static class Frame extends JFrame {
        public Frame() {
            this.setTitle("Internet Connection Error!");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            JOptionPane.showMessageDialog(this, "Cry about it :)))", "Internet Connection Error!", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
        }
    }
}
