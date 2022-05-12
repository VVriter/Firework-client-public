package com.firework;

import com.firework.AdminNotificator.Cfg;
import com.firework.AdminNotificator.DiscordSender;
import com.firework.Check.HwidCheck.JSonReader;
import com.firework.Check.HwidCheck.HwidManager;
import com.firework.Pastebin.exceptions.PasteException;

public class Checks {
    public static void main(String... args) throws PasteException {
        DiscordSender.doThing("```Installer is loaded by guy with OS NAME : > "+System.getProperty("os.name")+"```", Cfg.notify);
        HwidManager.hwidCheck();
    }
}
