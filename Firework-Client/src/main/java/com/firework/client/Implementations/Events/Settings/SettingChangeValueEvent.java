package com.firework.client.Implementations.Events.Settings;

import com.firework.client.Implementations.Settings.Setting;
import ua.firework.beet.Event;

public class SettingChangeValueEvent extends Event {

    public Setting setting;
    public SettingChangeValueEvent(Setting setting){
        this.setting = setting;
    }
}
