package com.firework.client.Implementations.Managers;

import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Objects;

public class RainbowManager extends Manager{

    public ArrayList<Setting<HSLColor>> settings;

    public RainbowManager() {
        super(true);
        settings = new ArrayList<>();
    }

    public boolean isRegistered(Setting<HSLColor> colorSetting){
        return settings.contains(colorSetting);
    }

    public void register(Setting<HSLColor> colorSetting){
        settings.add(colorSetting);
    }

    public void unRegister(Setting<HSLColor> colorSetting){
        settings.remove(colorSetting);
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        for(Setting<HSLColor> setting : settings) {
            float saturation = setting.getValue().saturation;
            float light = setting.getValue().light;
            float alpha = setting.getValue().alpha;

            float hue = setting.getValue().hue;
            hue++;
            if (hue > 360)
                hue -= 360;

            setting.setValue(new HSLColor(hue, saturation, light, alpha));
        }
    });
}
