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

    public static int getRainbow(final int speed, final int offset, final float s) {
        final float hue = (float)((System.currentTimeMillis() + offset) % speed);
        return Color.getHSBColor(hue / speed, s, 1.0f).getRGB();
    }

    /**
     * Applies a color via a lambda
     *
     * @param color the color
     * @param apply apply lambda
     */
    public static void applyColor(int color, ColorApply apply) {
        float alpha = (color >> 24 & 0xff) / 255f;
        float red = (color >> 16 & 0xff) / 255f;
        float green = (color >> 8 & 0xff) / 255f;
        float blue = (color & 0xff) / 255f;

        apply.apply(red, green, blue, alpha);
    }

    @FunctionalInterface
    public interface ColorApply {
        void apply(float r, float g, float b, float a);
    }
}
