package com.firework.Utilites.LoginCheck;

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

            JOptionPane.showMessageDialog(this, "INCORRECT PASSWORD/LOGIN", "Could not verify your AutInfo", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
        }

        /**
         * The convenience function to automatically copy the HWID to clipboard.
         */

    }
}
