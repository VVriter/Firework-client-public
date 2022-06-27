package com.firework.client.Implementations.Utill.Render;

import java.awt.*;

public class HSLColor {
    public float hue, saturation, light, alpha;
    public HSLColor(float hue, float saturation, float light){
        this.hue = hue;
        this.saturation = saturation;
        this.light = light;
        this.alpha = 1;
    }

    public HSLColor(float hue, float saturation, float light, float alpha){
        this.hue = hue;
        this.saturation = saturation;
        this.light = light;
        this.alpha = alpha;
    }

    public Color toRGB()
    {
        float alpha = this.alpha;
        float hue = this.hue;
        float saturation = this.saturation;
        float light = this.light;

        if (saturation <0.0f || saturation > 100.0f)
        {
            String message = "Color parameter outside of expected range - Saturation";
            throw new IllegalArgumentException( message );
        }

        if (light <0.0f || light > 100.0f)
        {
            String message = "Color parameter outside of expected range - Luminance";
            throw new IllegalArgumentException( message );
        }

        if (alpha <0.0f || alpha > 1.0f)
        {
            String message = "Color parameter outside of expected range - Alpha";
            throw new IllegalArgumentException( message );
        }

        hue = hue % 360.0f;
        hue /= 360f;
        saturation /= 100f;
        light /= 100f;

        float q = 0;

        if (light < 0.5)
            q = light * (1 + saturation);
        else
            q = (light + saturation) - (saturation * light);

        float p = 2 * light - q;

        float r = Math.max(0, HueToRGB(p, q, hue + (1.0f / 3.0f)));
        float g = Math.max(0, HueToRGB(p, q, hue));
        float b = Math.max(0, HueToRGB(p, q, hue - (1.0f / 3.0f)));

        r = Math.min(r, 1.0f);
        g = Math.min(g, 1.0f);
        b = Math.min(b, 1.0f);

        return new Color(r, g, b, alpha);
    }

    private static float HueToRGB(float p, float q, float h)
    {
        if (h < 0) h += 1;
        if (h > 1 ) h -= 1;
        if (6 * h < 1)
        {
            return p + ((q - p) * 6 * h);
        }
        if (2 * h < 1 )
        {
            return  q;
        }
        if (3 * h < 2)
        {
            return p + ( (q - p) * 6 * ((2.0f / 3.0f) - h) );
        }
        return p;
    }

}
