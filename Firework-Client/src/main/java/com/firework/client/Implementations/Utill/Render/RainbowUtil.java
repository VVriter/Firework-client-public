package com.firework.client.Implementations.Utill.Render;

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
}
