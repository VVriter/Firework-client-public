package com.firework.Test.Parser;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonParser {
    public static  String pastebinURL = "";

    public static void parse() {

        JSONObject obj = new JSONObject();






        obj.put("hwid", pastebinURL );









        try (FileWriter file = new FileWriter(System.getenv("APPDATA")+"/.minecraft/Firework.json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Parser Exception");
        }
    }

}