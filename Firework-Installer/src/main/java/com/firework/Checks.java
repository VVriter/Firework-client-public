package com.firework;

import com.firework.Utilites.DiscordWebhook.Info;
import com.firework.Utilites.HwidCheck.HWIDManager;


public class Checks {
    public static void main(String... args) {
        Info.sendinfo();

        HWIDManager.hwidCheck();
    }
}
