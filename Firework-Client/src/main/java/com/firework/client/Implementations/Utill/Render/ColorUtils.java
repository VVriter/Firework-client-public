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


}
