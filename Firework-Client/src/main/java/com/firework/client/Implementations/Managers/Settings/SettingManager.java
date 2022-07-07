package com.firework.client.Implementations.Managers.Settings;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Settings.Setting;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class SettingManager extends Manager {

    public ArrayList<Setting> settings;

    public SettingManager(){
        super(false);
        settings = new ArrayList<>();
    }

    public void updateSettingsByName(Setting setting){
        for(Setting _setting : settings){
            if(_setting.name == setting.name)
                _setting = setting;
        }
    }

    public Setting getSetting(Module module, String name){
        for(Setting setting : modulesSettings(module))
            if(setting.name == name)
                return setting;
        return null;
    }

    public Setting getSettingByName(String name){
        for(Setting setting : settings)
            if(setting.name == name)
                return setting;
        return null;
    }

    public ArrayList<Setting> modulesSettings(Module module){
        ArrayList<Setting> settings = new ArrayList<>();
        for(Setting setting : this.settings){
            if(setting.module.name == module.name)
                settings.add(setting);
        }
        return settings;
    }

    public ArrayList<String> settingsNames(){
        ArrayList<String> names = new ArrayList<>();
        for(Setting setting : this.settings){
            names.add(setting.name);
        }
        return names;
    }

    public ArrayList<String> settingsNames(Module module){
        ArrayList<String> names = new ArrayList<>();
        for(Setting setting : this.modulesSettings(module)){
            names.add(setting.name);
        }
        return names;
    }

}
