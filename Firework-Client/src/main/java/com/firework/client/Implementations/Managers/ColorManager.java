package com.firework.client.Implementations.Managers;

import com.firework.client.Features.Modules.Client.Gui;

import java.awt.*;

public class ColorManager extends Manager{
    public ColorManager() {
        super(false);
    }

    public Color getRomeo(){
        return Gui.Romeo.getValue().toRGB();
    }

    public Color getJuliet(){
        return Gui.Juliet.getValue().toRGB();
    }

    public Color getColor() {
        if(Gui.mode.getValue(Gui.modes.Single))
            return Gui.Color.getValue().toRGB();
        else
            return getJuliet();
    }

    public boolean gradient(){
        return Gui.mode.getValue(Gui.modes.Gradient);
    }
}
