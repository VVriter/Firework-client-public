package com.firework.client.Implementations.Utill.Render;

import java.awt.*;

public class HSLColor {
    public int hue, saturation, light;
    public HSLColor(int hue, int saturation, int light){
        this.hue = hue;
        this.saturation = saturation;
        this.light = light;
    }

    public Color toRGB(){
        return ColorUtils.hslColor(hue, saturation, light);
    }

}
