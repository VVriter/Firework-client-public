package com.firework.Utilites.LoginCheck;

import com.firework.GUI.DownloadFrame;

import com.firework.Utilites.Parser.HwidParser;

import com.firework.Utilites.Pastebin.exceptions.PasteException;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginManager {

    /**
     Your pastebin URL goes inside the empty string below.
     It should be a raw pastebin link, for example: pastebin.com/raw/pasteid
     */

    public static String whatcheck = "";
    static String loginreader;

    public static String pastebinURL = "https://pastebin.com/raw/TAp8xsKj";

    public static List<String> hwids = new ArrayList<>();

    static {
        try {
            loginreader = new Scanner(new URL(pastebinURL).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loginCheck()  {
        if(loginreader.contains(whatcheck)){
                try {
                    HwidParser.parseHwid();
                } catch (PasteException e) {
                    e.printStackTrace();
                }DownloadFrame.frame();
        }
    }}