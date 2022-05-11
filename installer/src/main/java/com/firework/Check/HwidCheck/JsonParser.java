package com.firework.Check.HwidCheck;



import org.json.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class JsonParser{
    public static String hwidlink = "";

    public static void parse() {

        JSONObject obj = new JSONObject();
        obj.put("HWID LINK",hwidlink);


        JSONArray list = new JSONArray();

        try (FileWriter file = new FileWriter(System.getenv("APPDATA")+"/.minecraft/Firework.json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSonReader.read();

    }

}