package com.firework.client.Implementations.Managers;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class ConfigManager{
    public String configDir = Firework.FIREWORK_DIRECTORY + "\\Configs\\";

    public ConfigManager(){
        new File(configDir).mkdirs();

        for(Module module : Firework.moduleManager.modules){
            loadModuleSettings(module);
        }
    }

    public void saveModuleSettings(Module module){
        //Stores settings to a JSONObject
        JSONArray jsonArray = new JSONArray();
        JSONObject config =  new JSONObject();
        for(Setting setting : Firework.settingManager.modulesSettings(module)){
            config.put(setting.name, setting.getValue());
        }
        jsonArray.add(config);

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
            file.write(jsonArray.toString());
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void loadModuleSettings(Module module){
        try {
            File configFile = new File(configDir + module.name + ".json");
            if (configFile.exists()) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(configFile));
                JSONArray configArray = (JSONArray) obj;
                for(Object object : configArray) {
                    JSONObject config = (JSONObject) object;
                    ArrayList<Setting> settings = new ArrayList<>();
                    for (int i = 0; i < Firework.settingManager.modulesSettings(module).size(); i++) {
                        Setting setting = Firework.settingManager.modulesSettings(module).get(0);
                        Firework.settingManager.settings.get(Firework.settingManager.settings.indexOf(Firework.settingManager.getSettingByName(setting.name))).setValue(config.get(setting.name));
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @SubscribeEvent
    public void settingEditEvent(SettingChangeValueEvent event){
        saveModuleSettings(event.setting.module);
    }
}
