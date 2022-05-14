package com.firework.client.Implementations.Managers.Settings;

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

}
