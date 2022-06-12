package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MuteManager {

    private static ArrayList<String> muted = new ArrayList<>();

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
        muted.add(name);
    }

    public static void removeFromMuteList(String name){
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Muted/"+name+".json");
        theDir.delete();
        muted.remove(name);
    }

    public static void getListOfNamesOfMutedPlayers(){
        File folder = new File(Firework.FIREWORK_DIRECTORY+"Muted");

        if(!folder.exists())
            folder.mkdir();

        if(folder.listFiles() == null) return;

        for (File file : folder.listFiles()) {
            String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
            System.out.println(fileNameWithOutExt);
            muted.add(fileNameWithOutExt);
            System.out.println(muted);
        }
    }

    public static boolean isMuted(String name) {
        for(String string : muted) {
            if(string.equals(name)) {
                return true;
            }
        }
        return false;
    }


}