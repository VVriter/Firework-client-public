package com.firework.Utilites.HwidCheck;

import com.firework.GUI.DownloadFrame;
import com.firework.GUI.LoginFrame;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HWIDManager {

    public static final String pastebinURL = "https://pastebin.com/raw/qd9gGN6n";

    public static List<String> hwids = new ArrayList<>();

    public static void hwidCheck() {
        hwids = URLReader.readURL();
        boolean isHwidPresent = hwids.contains(HwidUtill.getHwid());
        if (!isHwidPresent) {
           LoginError.Display();
        }else{LoginFrame.frame();}
    }
}