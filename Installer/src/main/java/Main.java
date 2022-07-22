import logging.Log;
import ui.LoginFrame;

public class Main {
    public static void main(String... args) {
        System.out.println("Hello World");
        Log.sendOkayInfo();
        Log.sendBadInfo();
        LoginFrame.frame();
    }
}
