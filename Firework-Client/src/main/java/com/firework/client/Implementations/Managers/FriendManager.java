package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FriendManager {
    public static void parse(String args){

        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Friends");
        if (!theDir.exists()){
            theDir.mkdirs();}

        JSONObject obj = new JSONObject();
        obj.put(args,args);
        try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Friends/" + args + ".json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public static void remove(String name){
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Friends/"+name+".json");
        theDir.delete();
    }

    public static void isFriend(){

    }
}
