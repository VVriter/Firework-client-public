package com.firework.client.Implementations.Managers.Text.CustomFontRenderer;

import com.firework.client.Implementations.Managers.Text.CustomFontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class CustomFontUtil {
    private static final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRenderer;
    public static CFontRenderer tcm = new CFontRenderer(getFontTTF("Tcm", 18), true, false);
    public static CustomFontRenderer customFontManager = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 18), false, true);
    private static Font getFontTTF(String name, int size) {
        Font font;
        try {
            InputStream is = CustomFontUtil.class.getResourceAsStream("/assets/minecraft/firework/fonts/Tcm.ttf");
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}