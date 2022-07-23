package com.firework.client.Implementations.Managers.Text;

import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Manager;
import net.minecraft.client.Minecraft;

import static java.lang.Math.round;

public class TextManager extends Manager {

    Minecraft mc = Minecraft.getMinecraft();

    public static boolean customFont = true;

    public TextManager() {
        super(false);
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public float drawString(String text, float x, float y, int color, boolean shadow) {
        if(customFont){
            Firework.customFontManager.drawString(text, x, y, color);
        }else {
            mc.fontRenderer.drawString(text, x, y, color, shadow);
        }
        return x;
    }

    public int getStringWidth(String text) {
        if(customFont){
            return (int) round(Firework.customFontManager.getWidth(text));
        }else {
            return mc.fontRenderer.getStringWidth(text);
        }
    }

    public int getFontHeight() {
        return mc.fontRenderer.FONT_HEIGHT;
    }

}
