package com.firework.client.Implementations.Managers.Text;

import com.firework.client.Firework;
import net.minecraft.client.Minecraft;

public class TextManager {

    public static boolean customFont = true;

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public float drawString(String text, float x, float y, int color, boolean shadow) {
        if(customFont){
            Firework.customFontManager.drawString(text, x, y, color);
        }else {
            Firework.minecraft.fontRenderer.drawString(text, x, y, color, shadow);
        }
        return x;
    }

    public int getStringWidth(String text) {
        return Firework.minecraft.fontRenderer.getStringWidth(text);
    }

    public int getFontHeight() {
        return Firework.minecraft.fontRenderer.FONT_HEIGHT;
    }

}
