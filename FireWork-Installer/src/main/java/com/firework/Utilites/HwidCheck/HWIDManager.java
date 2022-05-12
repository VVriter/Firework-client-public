package com.firework.Utilites.HwidCheck;

import com.firework.AuthCheck;
import com.firework.GUI.DownloadFrame;
import com.firework.GUI.LoginFrame;
import com.firework.Utilites.Parser.HwidParser;
import com.firework.Utilites.Parser.HwidUtill;
import com.firework.Utilites.Pastebin.exceptions.PasteException;
import io.vertx.core.impl.NoStackTraceThrowable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HWIDManager {


    public static String url = "";
    static String loginreader;

    static {
        try {
            loginreader = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loginCheck()  {
        if(loginreader.contains(HwidUtill.getHwid())){
            try{
                DownloadFrame.frame();}catch (Exception e){
                System.out.println("we have exception");
            }

        }
    }
}