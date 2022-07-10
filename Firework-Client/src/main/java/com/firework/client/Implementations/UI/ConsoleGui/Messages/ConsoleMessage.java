package com.firework.client.Implementations.UI.ConsoleGui.Messages;

import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;

import java.awt.*;

public class ConsoleMessage {
    public static void log(String prefix ,String text, double x1, float x2) {
        Firework.customFontManager.drawString("["+prefix+"] " + text, x1,x2, Color.WHITE.getRGB());
    }

    public static void error(String prefix ,String text, double x1, float x2) {
        Firework.customFontManager.drawString("["+prefix+"] " + text, x1,x2, Color.RED.getRGB());
    }

    public static void warning(String prefix ,String text, double x1, float x2) {
        Firework.customFontManager.drawString("["+prefix+"] " + text, x1,x2, Color.YELLOW.getRGB());
    }

    public static void rainbow(String prefix ,String text, double x1, float x2) {
        Firework.customFontManager.drawString("["+prefix+"] " + text, x1,x2, new Color(RainbowUtil.generateRainbowFadingColor(1,true)).getRGB());
    }
}
