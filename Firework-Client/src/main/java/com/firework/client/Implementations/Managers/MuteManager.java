package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MuteManager {
    public static void addMuted(String name){
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Muted");
        if (!theDir.exists()){
            theDir.mkdirs();}

        JSONObject obj = new JSONObject();
        obj.put(name,name);
        try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Muted/" + name + ".json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromMuteList(String name){
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Muted/"+name+".json");
        theDir.delete();
    }
}