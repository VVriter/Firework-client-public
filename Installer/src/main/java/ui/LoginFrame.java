package ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public static int width = 340;
    public static int height = 240;

    public static void  frame() {
        Dimension screenSize = Toolkit. getDefaultToolkit(). getScreenSize();

        JFrame loginFrame = new JFrame("Firework Installer");

        loginFrame.setBounds(
                new Rectangle(
                        (int) screenSize.getWidth()/2,
                        (int) screenSize.getHeight()/2,
                        width,
                        height
                )
        );

        loginFrame.setVisible(true);
    }
}
