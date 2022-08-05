package com.firework.client.Implementations.Utill.Render.Rounded;

import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class RoundRenderUtils {
        public static void roundedRectangle(Rectangle rectangle, double radius, int color) {
                glPushAttrib(GL_POINTS);
                glScaled(0.5, 0.5, 0.5);

                rectangle.x *= 2.0;
                rectangle.y *= 2.0;

                rectangle.width = (rectangle.width * 2.0) + rectangle.x;
                rectangle.height = (rectangle.height * 2.0) + rectangle.y;

                GlStateManager.enableBlend();
                glDisable(GL_TEXTURE_2D);
                RainbowUtil.applyColor(color, GL11::glColor4f);
                glEnable(GL_LINE_SMOOTH);

                glBegin(GL_POLYGON);

                double pi = Math.PI;

                int i;
                for (i = 0; i <= 90; ++i) {
                        glVertex2d(rectangle.x + radius + Math.sin(i * pi / 180.0) * radius * -1.0,
                                rectangle.y + radius + Math.cos(i * pi / 180.0) * radius * -1.0);
                }

                for (i = 90; i <= 180; ++i) {
                        glVertex2d(rectangle.x + radius + Math.sin(i * pi / 180.0) * radius * -1.0,
                                rectangle.height - radius + Math.cos(i * pi / 180.0) * radius * -1.0);
                }

                for (i = 0; i <= 90; ++i) {
                        glVertex2d(rectangle.width - radius + Math.sin(i * pi / 180.0) * radius,
                                rectangle.height - radius + Math.cos(i * pi / 180.0) * radius);
                }

                for (i = 90; i <= 180; ++i) {
                        glVertex2d(rectangle.width - radius + Math.sin(i * pi / 180.0) * radius, rectangle.y + radius + Math.cos(i * pi / 180.0) * radius);
                }

                glEnd();
                glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                glEnable(GL_TEXTURE_2D);
                glDisable(GL_LINE_SMOOTH);
                GlStateManager.disableBlend();

                glScaled(2.0, 2.0, 0.0);
                glPopAttrib();
        }


        public static ShaderUtil roundedShader = new ShaderUtil("roundedRect");
        public static ShaderUtil roundedOutlineShader = new ShaderUtil("firework/shaders/roundRectOutline.frag");
        private static final ShaderUtil roundedTexturedShader = new ShaderUtil("firework/shaders/roundRectTextured.frag");
        private static final ShaderUtil roundedGradientShader = new ShaderUtil("roundedRectGradient");


        public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
                drawRound(x, y, width, height, radius, false, color);
        }

        public static void drawRoundScale(float x, float y, float width, float height, float radius, Color color, float scale) {
                drawRound(x + width - width * scale, y + height / 2f - ((height / 2f) * scale),
                        width * scale, height * scale, radius, false, color);
        }

        public static void drawGradientHorizontal(float x, float y, float width, float height, float radius, Color left, Color right) {
                drawGradientRound(x, y, width, height, radius, left, left, right, right);
        }
        public static void drawGradientVertical(float x, float y, float width, float height, float radius, Color top, Color bottom) {
                drawGradientRound(x, y, width, height, radius, bottom, top, bottom, top);
        }
        public static void drawGradientCornerLR(float x, float y, float width, float height, float radius, Color topLeft, Color bottomRight) {
                Color mixedColor = interpolateColorC(topLeft, bottomRight, .5f);
                drawGradientRound(x, y, width, height, radius, mixedColor, topLeft, bottomRight, mixedColor);
        }

        public static void drawGradientCornerRL(float x, float y, float width, float height, float radius, Color bottomLeft, Color topRight) {
                Color mixedColor = interpolateColorC(topRight, bottomLeft, .5f);
                drawGradientRound(x, y, width, height, radius, bottomLeft, mixedColor, mixedColor, topRight);
        }

        public static void drawGradientRound(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
                resetColor();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                roundedGradientShader.init();
                setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
                // Bottom Left
                roundedGradientShader.setUniformf("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f, bottomLeft.getAlpha() / 255f);
                //Top left
                roundedGradientShader.setUniformf("color2", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f, topLeft.getAlpha() / 255f);
                //Bottom Right
                roundedGradientShader.setUniformf("color3", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f, bottomRight.getAlpha() / 255f);
                //Top Right
                roundedGradientShader.setUniformf("color4", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f, topRight.getAlpha() / 255f);
                ShaderUtil.drawQuads(x - 1, y - 1, width + 2, height + 2);
                roundedGradientShader.unload();
                GlStateManager.disableBlend();
        }




        public static void drawRound(float x, float y, float width, float height, float radius, boolean blur, Color color) {
                resetColor();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                roundedShader.init();

                setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
                roundedShader.setUniformi("blur", blur ? 1 : 0);
                roundedShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

                ShaderUtil.drawQuads(x - 1, y - 1, width + 2, height + 2);
                roundedShader.unload();
                GlStateManager.disableBlend();
        }

        public static void drawRoundTextured(float x, float y, float width, float height, float radius, float alpha) {
                resetColor();
                roundedTexturedShader.init();
                roundedTexturedShader.setUniformi("textureIn", 0);
                setupRoundedRectUniforms(x, y, width, height, radius, roundedTexturedShader);
                roundedTexturedShader.setUniformf("alpha", alpha);
                ShaderUtil.drawQuads(x - 1, y - 1, width + 2, height + 2);
                roundedTexturedShader.unload();
                GlStateManager.disableBlend();
        }

        private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                roundedTexturedShader.setUniformf("location", x * sr.getScaleFactor(),
                        (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
                roundedTexturedShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
                roundedTexturedShader.setUniformf("radius", radius * sr.getScaleFactor());
        }

        public static void resetColor() {
                GlStateManager.color(1, 1, 1, 1);
        }

        public static Color interpolateColorC(Color color1, Color color2, float amount) {
                amount = Math.min(1, Math.max(0, amount));
                return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount),
                        interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                        interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                        interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
        }

        public static Double interpolate(double oldValue, double newValue, double interpolationValue){
                return (oldValue + (newValue - oldValue) * interpolationValue);
        }

        public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
                return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
        }

        public static double round(double num, double increment) {
                BigDecimal bd = new BigDecimal(num);
                bd = (bd.setScale((int) increment, RoundingMode.HALF_UP));
                return bd.doubleValue();
        }

        public static double round(double value, int places) {
                if (places < 0) {
                        throw new IllegalArgumentException();
                }
                BigDecimal bd = new BigDecimal(value);
                bd = bd.setScale(places, RoundingMode.HALF_UP);
                return bd.doubleValue();
        }

        public static float getRandomFloat(float max, float min) {
                SecureRandom random = new SecureRandom();
                return random.nextFloat() * (max - min) + min;
        }

        public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
                resetColor();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                roundedOutlineShader.init();

                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
                roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * sr.getScaleFactor());
                roundedOutlineShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                roundedOutlineShader.setUniformf("outlineColor", outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);


                ShaderUtil.drawQuads(x - (2 + outlineThickness), y - (2 + outlineThickness), width + (4 + outlineThickness * 2), height + (4 + outlineThickness * 2));
                roundedOutlineShader.unload();
                GlStateManager.disableBlend();
        }

}
