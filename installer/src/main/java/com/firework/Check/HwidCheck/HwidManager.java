package com.firework.Check.HwidCheck;

import com.firework.AdminNotificator.Cfg;
import com.firework.AdminNotificator.DiscordSender;
import com.firework.Check.HwidCheck.HwidParse;
import com.firework.Check.HwidCheck.JSonReader;
import com.firework.Check.HwidCheck.JsonParser;
import com.firework.Frames.DownloadFrame;
import com.firework.Frames.LoginFrame;
import com.firework.LoginManager.Exception;
import com.firework.LoginManager.URLReader;

import java.util.ArrayList;
import java.util.List;

public class HwidManager {
    public static String morgen = "";
    public static final String pastebinURL = "https://pastebin.com/raw/hUWGkzUb";

    public static List<String> hwids = new ArrayList<>();

    public static void hwidCheck() {
        System.out.println(morgen);
        System.out.println(HwidParse.getSystemInfo());
        hwids = URLReader.readURL();
        boolean isHwidPresent = hwids.contains(HwidParse.getSystemInfo());
        if (!isHwidPresent) {
            System.out.println("We have exception");
            LoginFrame.frame();
        }
    }

}
