package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;

import java.awt.Color;


public class CustomEnchants extends Module {
        public static Setting<HSLColor> color = null;
        public static Setting<Boolean> enabled = null;
    public CustomEnchants(){super("CustomEnchants",Category.RENDER);
        color = new Setting<>("Color", new HSLColor(1, 54, 43), this);
        enabled = this.isEnabled;
    }
    public static Color getColor(long offset, float fade) {
        float hue = (float)(System.nanoTime() + offset) / 1.0E10f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0f * fade, (float)c.getGreen() / 255.0f * fade, (float)c.getBlue() / 255.0f * fade, (float)c.getAlpha() / 255.0f);
    }

}
