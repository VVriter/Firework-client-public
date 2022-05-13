package com.firework.client.Managers.Parser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonParser {
    public static void parse() {

        JSONObject obj = new JSONObject();
        obj.put("Firework", "bebra");

        File theDir = new File(System.getenv("APPDATA") + "\\.minecraft\\" +"\\Firework\\");
        if (!theDir.exists()){
            theDir.mkdirs();
            try (FileWriter file = new FileWriter(System.getenv("APPDATA") + "\\.minecraft\\" +"\\Firework\\"+ "Firework.json")) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
