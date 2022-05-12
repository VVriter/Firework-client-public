package com.firework.Utilites.HwidCheck;

import javax.swing.*;

public class LoginError {
    public static void Display() {
        Frame frame = new Frame();
        frame.setVisible(false);
    }

    /**
     * The frame if the HWID check was unsuccessful.
     */

    public static class Frame extends JFrame {
        public Frame() {
            this.setTitle("Verification failed.");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);

            JOptionPane.showMessageDialog(this, "Hwid does not matched dm me in discord to get help s1ash#4203", "Could not verify your Hwid", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
        }

        /**
         * The convenience function to automatically copy the HWID to clipboard.
         */

    }
}
