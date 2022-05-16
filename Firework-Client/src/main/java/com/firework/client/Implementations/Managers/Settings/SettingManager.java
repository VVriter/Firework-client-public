package com.firework.client.Implementations.Managers.Settings;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

import java.util.ArrayList;

public class SettingManager {

    public ArrayList<Setting> settings;

    public SettingManager(){
        settings = new ArrayList<>();
    }

    public void updateSettingsByName(Setting setting){
        for(Setting _setting : settings){
            if(_setting.name == setting.name)
                _setting = setting;
        }
    }

    public ArrayList<Setting> modulesSettings(Module module){
        ArrayList<Setting> settings = new ArrayList<>();
        for(Setting setting : this.settings){
            if(setting.module.name == module.name & setting.hidden == false)
                settings.add(setting);
        }

        return  settings;
    }

}
