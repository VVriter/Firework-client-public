package com.firework.client.Implementations.Managers.Text;

import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class CustomFontManager {
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("§[0123456789abcdefklmnor]");
    public final int FONT_HEIGHT = 9;
    private final Map<String, Float> cachedStringWidth = new HashMap<>();
    private float antiAliasingFactor;
    private UnicodeFont unicodeFont;
    private int prevScaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    public static final char COLOR_CHAR = '\u00A7';
    private final String name;
    private final float size;

    public CustomFontManager(String fontName, float fontSize) {
        name = fontName;
        size = fontSize;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

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

        this.antiAliasingFactor = resolution.getScaleFactor();
    }

    public CustomFontManager(Font font) {
        this(font.getFontName(), font.getSize());
    }

    public CustomFontManager(String fontName, int fontType, int size) {
        this(new Font(fontName, fontType, size));
    }

    private Font getFontByName(String name) throws IOException, FontFormatException {
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

    private Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, CustomFontManager.class.getResourceAsStream(path));
    }

    public int drawString(String text, float x, float y, int color) {
        if (text == null) {
            return 0;
        }

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        try {
            if (resolution.getScaleFactor() != prevScaleFactor) {
                prevScaleFactor = resolution.getScaleFactor();
                unicodeFont = new UnicodeFont(getFontByName(name).deriveFont(size * prevScaleFactor / 2));
                unicodeFont.addAsciiGlyphs();
                unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                unicodeFont.loadGlyphs();
            }
        } catch (FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }

        this.antiAliasingFactor = resolution.getScaleFactor();

        GlStateManager.pushMatrix();
        GlStateManager.scale(1 / antiAliasingFactor, 1 / antiAliasingFactor, 1 / antiAliasingFactor);
        x *= antiAliasingFactor;
        y *= antiAliasingFactor;
        float originalX = x;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);

        char[] characters = text.toCharArray();

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        String[] parts = COLOR_CODE_PATTERN.split(text);
        int index = 0;
        for (String s : parts) {
            for (String s2 : s.split("\n")) {
                for (String s3 : s2.split("\r")) {

                    unicodeFont.drawString(x, y, s3, new org.newdawn.slick.Color(color));
                    x += unicodeFont.getWidth(s3);

                    index += s3.length();
                    if (index < characters.length && characters[index] == '\r') {
                        x = originalX;
                        index++;
                    }
                }
                if (index < characters.length && characters[index] == '\n') {
                    x = originalX;
                    y += getHeight(s2) * 2;
                    index++;
                }
            }
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
        return (int) x;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0x000000);
        return drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - ((int) getWidth(text) >> 1), y, color);
    }

    public static String stripColor(final String input) {
        return input == null ? null
                : Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]").matcher(input).replaceAll("");
    }

    public float getWidth(String text) {
        if (cachedStringWidth.size() > 1000) {
            cachedStringWidth.clear();
        }
        return cachedStringWidth.computeIfAbsent(text,
                e -> unicodeFont.getWidth(stripColor(text)) / antiAliasingFactor);
    }

    public float getHeight(String s) {
        return unicodeFont.getHeight(s) / 2.0F;
    }

    public UnicodeFont getFont() {
        return unicodeFont;
    }

    public List<String> splitString(String text, int wrapWidth) {
        List<String> lines = new ArrayList<>();

        String[] splitText = text.split(" ");
        StringBuilder currentString = new StringBuilder();

        for (String word : splitText) {
            String potential = currentString + " " + word;

            if (getWidth(potential) >= wrapWidth) {
                lines.add(currentString.toString());
                currentString = new StringBuilder();
            }

            currentString.append(word).append(" ");
        }

        lines.add(currentString.toString());
        return lines;
    }
}