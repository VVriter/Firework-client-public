package com.firework.client.Implementations.Events.Settings;

import com.firework.client.Implementations.Settings.Setting;
import ua.firework.beet.Event;

public class SettingModifyValueEvent extends Event {

    public Setting setting;
    public SettingModifyValueEvent(Setting setting){
        this.setting = setting;
    }
}
