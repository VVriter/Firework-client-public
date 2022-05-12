package com.firework.LoginManager;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Exception extends Throwable {

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
                JOptionPane.showMessageDialog(this, "Ur password/login is incorrect, cry abot it or dm me in discord s1ash#4203", "Login excrption!!!", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            }

            /**
             * The convenience function to automatically copy the HWID to clipboard.
             */
        }
}
