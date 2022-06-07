package com.firework.Utilites.LoginCheck;

import com.firework.GUI.DownloadFrame;
import com.firework.GUI.LoginFrame;
import com.firework.Utilites.LoginCheck.URLReader;


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

    public static String pastebinURL = "https://pastebin.com/raw/TAp8xsKj";

    public static List<String> hwids = new ArrayList<>();


    public static void hwidCheck() {
        hwids = URLReader.readURL();
        boolean isHwidPresent = hwids.contains(whatcheck);
        if (!isHwidPresent) {
            LoginError.Display();
        }else{DownloadFrame.frame();}
    }

    }