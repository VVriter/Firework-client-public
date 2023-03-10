package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MuteManager extends Manager{

    private static ArrayList<String> muted = new ArrayList<>();

    public MuteManager() {
        super(false);
    }

    public static void addMuted(String name){
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Muted");
        if (!theDir.exists()){
            theDir.mkdirs();}

        JsonObject obj = new JsonObject();
        obj.addProperty(name,name);
        try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Muted/" + name + ".json")) {
            new GsonBuilder().setPrettyPrinting().create().toJson(obj, file);
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