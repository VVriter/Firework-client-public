package com.firework;

import com.firework.Utilites.DiscordWebhook.Info;
import com.firework.Utilites.HwidCheck.HWIDManager;
import com.firework.Utilites.Parser.HwidParser;
import com.firework.Utilites.Parser.JsonParsReader;
import com.firework.Utilites.Parser.JsonParser;
import com.firework.Utilites.Pastebin.exceptions.PasteException;

public class Checks {
    public static void main(String... args) throws PasteException {
        Info.sendinfo();

        AuthCheck.read();
    }
}
