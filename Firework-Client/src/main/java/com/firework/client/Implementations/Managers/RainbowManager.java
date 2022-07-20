package com.firework.client.Implementations.Managers;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class RainbowManager extends Manager{

    public ArrayList<Setting<HSLColor>> settings;

    public RainbowManager() {
        super(true);
        settings = new ArrayList<>();
    }

    public boolean isRegistered(Setting<HSLColor> colorSetting){
        for(Setting setting : settings)
            if(setting.name == colorSetting.name && setting.module.name == colorSetting.module.name)
                return true;
        return false;
    }

    public void register(Setting<HSLColor> colorSetting){
        settings.add(colorSetting);
    }

    public void unRegister(Setting<HSLColor> colorSetting){
        Setting toRemove = null;
        for(Setting setting : settings)
            if(setting.name == colorSetting.name && setting.module.name == colorSetting.module.name)
                toRemove = colorSetting;
        if(toRemove == null) return;
        settings.remove(toRemove);
    }

    @SubscribeEvent
    public void onTick(final TickEvent.RenderTickEvent event){
        for(Setting<HSLColor> setting : settings) {
            float saturation = setting.getValue().saturation;
            float light = setting.getValue().light;

            float hue = setting.getValue().hue;
            hue++;
            if (hue > 360)
                hue -= 360;

            setting.setValue(new HSLColor(hue, saturation, light));
        }
    }
}
