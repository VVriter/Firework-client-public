package com.firework.client.Implementations.Managers.Text.font;

import com.firework.client.Implementations.Managers.Text.CustomFontManager;
import com.firework.client.Implementations.Managers.Text.CustomFontRenderer.CustomFontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.firework.client.Implementations.Utill.Util.mc;

@SuppressWarnings("NonAtomicOperationOnVolatileField")
public class FontUtil {
    public static volatile int completed;
    public static MinecraftFontRenderer normal;
    private static Font normal_ = getUnicodeFont("Tcm", 18).getFont();
    private static int prevScaleFactor = new ScaledResolution(mc).getScaleFactor();
    public static final char COLOR_CHAR = '\u00A7';

    private static UnicodeFont getUnicodeFont(String fontName, float fontSize) {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        UnicodeFont unicodeFont = null;
        try {
            prevScaleFactor = resolution.getScaleFactor();
            unicodeFont = new UnicodeFont(
                    getFontByName(fontName).deriveFont(fontSize * prevScaleFactor / 2));
            unicodeFont.addAsciiGlyphs();
            unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
            unicodeFont.loadGlyphs();
        } catch (FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }

    private static Font getFontByName(String name) throws IOException, FontFormatException {
        if (name.equalsIgnoreCase("roboto condensed")) {
            return getFontFromInput("/assets/minecraft/firework/fonts/RobotoCondensed-Regular.ttf");
        } else if (name.equalsIgnoreCase("roboto")) {
            return getFontFromInput("/assets/minecraft/firework/fonts/Roboto-Regular.ttf");
        } else if (name.equalsIgnoreCase("roboto medium")) {
            return getFontFromInput("/assets/minecraft/firework/fonts/Roboto-Medium.ttf");
        } else if (name.equalsIgnoreCase("montserrat")) {
            return getFontFromInput("/assets/minecraft/firework/fonts/Montserrat-Regular.ttf");
        } else if (name.equalsIgnoreCase("segoeui") || name.equalsIgnoreCase("segoeui light")) {
            return getFontFromInput("/assets/minecraft/firework/fonts/SegoeUI-Light.ttf");
        } else if (name.equalsIgnoreCase("jellee bold")) {
            return getFontFromInput("/assets/minecraft/firework/fonts/Jellee-Bold.ttf");
        } else if (name.equalsIgnoreCase("tcm")) {
            return getFontFromInput("/assets/minecraft/firework/fonts/Tcm.ttf");
        } else {
            // Need to return the default font.
            return getFontFromInput("assets/fonts/Roboto-Regular.ttf");
        }
    }

    private static Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, FontUtil.class.getResourceAsStream(path));
    }

    public static void bootstrap() {
        normal = new MinecraftFontRenderer(normal_, 18,true, true);
    }
}