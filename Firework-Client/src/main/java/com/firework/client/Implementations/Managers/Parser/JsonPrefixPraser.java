package com.firework.client.Implementations.Managers.Parser;

import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class JsonPrefixPraser {
    public static void parse() {

        //Creates new JSon object
        JSONObject obj = new JSONObject();
        obj.put("Prefix", "*");


        //Creates dir if it really needed
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Prefix.json");
        if (!theDir.exists()){
            try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Prefix.json")) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
