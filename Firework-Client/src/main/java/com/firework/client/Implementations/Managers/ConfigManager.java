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

public class ConfigManager extends Manager{
    public String configDir = Firework.FIREWORK_DIRECTORY + "/Configs/";

    public ConfigManager(){
        super(true);
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
            config.put(setting.name, String.valueOf(setting.getValue()));
        }
        jsonArray.add(config);

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
            file.write(jsonArray.toString());
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void loadModuleSettings(Module module){
        try {
            //Config file obj declaration
            File configFile = new File(configDir + module.name + ".json");
            if (configFile.exists()) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(configFile));
                JSONArray configArray = (JSONArray) obj;
                //For loop for each array (there only one usually)
                for(Object object : configArray) {
                    JSONObject config = (JSONObject) object;
                    //Loads values for each setting
                    for (int i = 0; i < Firework.settingManager.modulesSettings(module).size(); i++) {;
                        Setting setting1 = Firework.settingManager.settings.get(Firework.settingManager.settings.indexOf(Firework.settingManager.modulesSettings(module).get(i)));

                        if(setting1.value instanceof Integer)
                            setting1.setValue(new Integer(String.valueOf(config.get(setting1.name))).intValue());
                        else if(setting1.value instanceof Double)
                            setting1.setValue(new Double(String.valueOf(config.get(setting1.name))).doubleValue());
                        else if(setting1.value instanceof Boolean)
                            setting1.setValue(Boolean.parseBoolean(String.valueOf(config.get(setting1.name))));
                        else if(setting1.value instanceof Enum)
                            setting1.setValue(((Enum)setting1.getValue()).valueOf(((Enum)setting1.getValue()).getClass(), String.valueOf(config.get(setting1.name))));

                    }
                }
                if(module.isEnabled.getValue())
                    module.onEnable();
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
