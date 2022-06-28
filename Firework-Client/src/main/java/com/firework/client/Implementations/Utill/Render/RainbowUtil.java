package com.firework.client.Implementations.Utill.Render;

import net.minecraft.client.Minecraft;

import java.awt.*;

public class RainbowUtil {
    public static final float DEFAULT_COLOR_SATURATION = 0.95F;
    public static final float DEFAULT_COLOR_BRIGHTNESS = 0.95F;
    public static int generateRainbowFadingColor(float offset, boolean drastic) {
        //long offset_ = (drastic ? 200000000L : 20000000L) * offset;
        long offset_ = (long) ((drastic ? 200000000L : 20000000L) * offset);
        float hue = (System.nanoTime() + offset_) / 4.0E9f % 1.0F;
        return (int) Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, DEFAULT_COLOR_SATURATION, DEFAULT_COLOR_BRIGHTNESS)), 16);

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
