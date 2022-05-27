package com.firework.client.Implementations.Utill.Render;

import java.awt.*;

public class ColorUtils {

    public static Color plus(Color one, Color two){
        return new Color(one.getRed() + two.getRed(), one.getGreen() + two.getGreen(), one.getBlue() + two.getBlue());
    }

    public static Color minus(Color one, Color two){
        return new Color(one.getRed() - two.getRed(), one.getGreen() - two.getGreen(), one.getBlue() - two.getBlue());
    }

    public static int astolfoColors(int yOffset, int yTotal) {
        float hue;
        float speed = 2900.0f;
        for (hue = (float) (System.currentTimeMillis() % (long) ((int)speed)) + (float) ((yTotal - yOffset) * 9); hue > speed; hue -= speed) {}
        if ((double) (hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.HSBtoRGB(hue += 0.5f, 0.5f, 1.0f);
    }

    public static Color hslColor(float h, float s, float l) {
        float q, p, r, g, b;

        if (s == 0) {
            r = g = b = l; // achromatic
        } else {
            q = l < 0.5 ? (l * (1 + s)) : (l + s - l * s);
            p = 2 * l - q;
            r = hue2rgb(p, q, h + 1.0f / 3);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1.0f / 3);
        }
        return new Color(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }

    public static float hue2rgb(float p, float q, float h) {
        if (h < 0) {
            h += 1;
        }

        if (h > 1) {
            h -= 1;
        }

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }

}
