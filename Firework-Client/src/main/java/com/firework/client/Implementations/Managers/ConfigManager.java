package com.firework.client.Implementations.Managers;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.google.gson.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.*;

public class ConfigManager extends Manager{
    public String configDir = Firework.FIREWORK_DIRECTORY + "Configs/";

    public ConfigManager(){
        super(true);
        new File(configDir).mkdirs();

        for(Module module : Firework.moduleManager.modules){
            loadModuleSettings(module);
        }
    }

    public void saveModuleSettings(Module module){
        //Stores settings to a JSONObject
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject config = new JsonObject();
        for(Setting setting : Firework.settingManager.modulesSettings(module)){
            config.addProperty(setting.name, String.valueOf(setting.getValue()));
        }

        //Config file declaration
        File configFile = new File(configDir + module.name + ".json");
        try {
            //Creates file config if didn't create one
            if (!configFile.exists())
                configFile.createNewFile();
        }catch (Exception e) {
            System.out.println(e);
        }
        //Writes module settings to a file
        try (FileWriter file = new FileWriter(configFile)) {
            gson.toJson(config, file);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void loadModuleSettings(Module module){
        //Config file obj declaration
        File configFile = new File(configDir + module.name + ".json");
        if (configFile.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(configFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            JsonParser parser = new JsonParser();

            JsonObject config = new Gson().fromJson(parser.parse(reader), JsonObject.class);

            //Loads values for each setting
            for (int i = 0; i < Firework.settingManager.modulesSettings(module).size(); i++) {;
                    Setting setting1 = Firework.settingManager.settings.get(Firework.settingManager.settings.indexOf(Firework.settingManager.modulesSettings(module).get(i)));

                    if(setting1.value instanceof Integer)
                        setting1.setValueNoEvent(config.get(setting1.name).getAsInt());
                    else if(setting1.value instanceof Double)
                        setting1.setValueNoEvent(config.get(setting1.name).getAsDouble());
                    else if(setting1.value instanceof Boolean)
                        setting1.setValueNoEvent(config.get(setting1.name).getAsBoolean());
                    else if(setting1.value instanceof String)
                        setting1.setValueNoEvent(config.get(setting1.name).getAsString());
                    else if(setting1.value instanceof Enum)
                        setting1.setEnumValueNoEvent(config.get(setting1.name).getAsString());
                }
        }
    }

    @SubscribeEvent
    public void settingEditEvent(SettingChangeValueEvent event){
        saveModuleSettings(event.setting.module);
    }
}
