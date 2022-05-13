package com.firework.client.Managers.Settings;

import com.firework.client.Settings.Setting;

import java.util.ArrayList;

public class SettingManager {

    public ArrayList<Setting> settings;

    public SettingManager(){
        settings = new ArrayList<>();
    }

    public void updateSettingsByName(Setting setting){
        for(Setting st : settings){
            if(st.name == setting.name)
                st = setting;
        }
    }

}
