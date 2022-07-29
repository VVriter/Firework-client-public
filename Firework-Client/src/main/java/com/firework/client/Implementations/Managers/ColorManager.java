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
}
