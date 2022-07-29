package com.firework.client.Implementations.Managers;

import com.firework.client.Features.Modules.Client.Settings;

import java.awt.*;

public class ColorManager extends Manager{
    public ColorManager() {
        super(false);
    }

    public Color getRomeo(){
        return Settings.Romeo.getValue().toRGB();
    }

    public Color getJuliet(){
        return Settings.Juliet.getValue().toRGB();
    }

    public Color getColor() {
        if(Settings.mode.getValue(Settings.modes.Single))
            return Settings.Color.getValue().toRGB();
        else
            return getJuliet();
    }

    public boolean gradient(){
        return Settings.mode.getValue(Settings.modes.Gradient);
    }
}
