import logging.DiscordWebhook;
import ui.LoginFrame;

import java.io.IOException;

public class Main {
    public static void main(String... args) {
        System.out.println("Hello World");
        LoginFrame.frame();

        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/988402463214276638/eJY0-19aDlM85ILgsqrwa-OrPQklqQKXlRDKRGrtnn8ycYiEl966bH83V8gFm5lyTuLH");
        webhook.setContent("Bebro");
        try {
            webhook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
