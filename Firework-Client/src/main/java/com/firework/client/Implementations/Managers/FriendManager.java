package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FriendManager extends Manager{

    public static ArrayList<String> friends = new ArrayList<>();

    public FriendManager() {
        super(false);
    }

    public static void parse(String args){
        friends.add(args);
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


    public static void getFriends(){
        File folder = new File(Firework.FIREWORK_DIRECTORY+"Friends");

        if(!folder.exists())
            folder.mkdir();

        if(folder.listFiles() == null) return;

        for (File file1 : folder.listFiles()) {
            String fileNameWithOutExt = FilenameUtils.removeExtension(file1.getName());
            System.out.println(fileNameWithOutExt);
            friends.add(fileNameWithOutExt);
            System.out.println(friends);
        }
    }

    public static void remove(String name){
        friends.remove(name);
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Friends/"+name+".json");
        theDir.delete();
    }
}
