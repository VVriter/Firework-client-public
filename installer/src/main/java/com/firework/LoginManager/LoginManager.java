package com.firework.LoginManager;

import com.firework.AdminNotificator.Cfg;
import com.firework.AdminNotificator.DiscordSender;

import java.util.ArrayList;
import java.util.List;

public class LoginManager {
    public static final String pastebinURL = "https://pastebin.com/raw/TAp8xsKj";

    public static List<String> hwids = new ArrayList<>();

    public static void passCheck() {
        hwids = URLReader.readURL();
        boolean isHwidPresent = hwids.contains(megabebra);
        if (!isHwidPresent) {
            Exception.Display();
        }
        DiscordSender.doThing("```Someone logged in to installer with login data: "+ megabebra+"```", Cfg.notify);
    }

    public static String megabebra = "";
}
