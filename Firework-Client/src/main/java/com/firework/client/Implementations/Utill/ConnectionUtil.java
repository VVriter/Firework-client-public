package com.firework.client.Implementations.Utill;

import net.minecraftforge.fml.common.FMLLog;
import xyz.firework.autentification.InternetConnectionError;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionUtil {

    public static boolean isInternetConnected(){
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void checkForInternetConnection(){
        if(isInternetConnected() == true){
            //ok
        }else{
            InternetConnectionError.Display();
        }
    }
}