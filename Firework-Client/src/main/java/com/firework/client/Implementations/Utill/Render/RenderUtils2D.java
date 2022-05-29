package com.firework.client.Implementations.Utill.Render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static java.awt.Color.*;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

public class RenderUtils2D {

    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();

    public static void drawGradientRect(Rectangle rectangle, Color startColor, Color endColor)
    {
        double zLevel=0.0;

        float f = (float) startColor.getRed() / 255.0f;
        float f1 = (float) startColor.getGreen() / 255.0f;
        float f2 = (float) startColor.getBlue() / 255.0f;
        float f3 = (float) startColor.getAlpha() / 255.0f;
        float f4 = (float) endColor.getRed() / 255.0f;
        float f5 = (float) endColor.getGreen() / 255.0f;
        float f6 = (float) endColor.getBlue() / 255.0f;
        float f7 = (float) endColor.getAlpha() / 255.0f;

        int x = rectangle.x;
        int y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)w, (double)h, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)w, (double)y, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)x, (double)y, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRectangle(Rectangle rectangle, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(1);

        int x = rectangle.x;
        int y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y + h, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + w, rectangle.y + h, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + w, rectangle.y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x, rectangle.y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRectangleOutline(Rectangle rectangle, float width, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(width);
        int x = rectangle.x;
        int y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;

        bufferbuilder.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y + h, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + w, rectangle.y + h, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + w, rectangle.y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x, y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircleOutline(Point o, float radius, float width, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(width);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        bufferbuilder.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

        for (int a = 0; a < 360; a++) {
            final double x1 = o.x + (radius * Math.sin(Math.toRadians(a)));
            final double z1 = o.y + (radius * Math.cos(Math.toRadians(a)));

            bufferbuilder.pos(x1, z1, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        }

        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        glDisable(GL_LINE_SMOOTH);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawFilledCircle(Point point, Color color, int r)
    {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        bufferbuilder.begin(GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

        bufferbuilder.pos(point.getX(), point.getY(), 0.0D).color(white.getRed(), white.getGreen(), white.getBlue(), white.getAlpha()).endVertex();

        for (int i = 0; i <= 360; i++)
        {
            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * Math.PI) / 180)) * r;

            bufferbuilder.pos(point.getX() + x2, point.getY() + y2, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        }
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawColorPickerBase(Point point, HSLColor hslColor, int r)
    {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        bufferbuilder.begin(GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

        bufferbuilder.pos(point.getX(), point.getY(), 0.0D).color(white.getRed(), white.getGreen(), white.getBlue(), white.getAlpha()).endVertex();

        for (int i = 0; i <= 360; i++)
        {
            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * Math.PI) / 180)) * r;

            HSLColor color = new HSLColor(i + 1, hslColor.saturation, hslColor.light);
            Color colorRGB = color.toRGB();

            bufferbuilder.pos(point.getX() + x2, point.getY() + y2, 0.0D).color(colorRGB.getRed(), colorRGB.getGreen(), colorRGB.getBlue(), colorRGB.getAlpha()).endVertex();
        }
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawMarker(Rectangle rectangle, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(1);

        int x = rectangle.x;
        int y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;

        bufferbuilder.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x - 3, y+3, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + 3, y+3, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x - 3, y+12, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + 3, y+12, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static double distance(Point one, Point two){
        double ac = abs(two.getY() - one.getY());
        double cb = abs(two.getX() - one.getX());

        return sqrt(ac * ac + cb * cb);
    }
}
