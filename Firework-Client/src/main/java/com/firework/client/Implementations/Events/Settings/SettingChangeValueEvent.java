package com.firework.client.Implementations.Events.Settings;

import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SettingChangeValueEvent extends Event {

    public Setting setting;
    public SettingChangeValueEvent(Setting setting){
        this.setting = setting;
    }
}
