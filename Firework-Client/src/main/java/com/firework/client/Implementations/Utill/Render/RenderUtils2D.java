package com.firework.client.Implementations.Utill.Render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

import static java.awt.Color.*;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

public class RenderUtils2D {

    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();

    public static void drawGradientRectVertical(Rectangle rectangle, Color startColor, Color endColor)
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
        bufferbuilder.pos((double)x, (double)y + h, (double)zLevel).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos((double)x + w, (double)y + h, (double)zLevel).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos((double)x + w, (double)y, (double)zLevel).color(f4, f5, f6, f7).endVertex();
        bufferbuilder.pos((double)x, (double)y, (double)zLevel).color(f4, f5, f6, f7).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientRectHorizontal(Rectangle rectangle, Color startColor, Color endColor)
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
        bufferbuilder.pos((double)x, (double)y + h, (double)zLevel).color(f4, f5, f6, f7).endVertex();
        bufferbuilder.pos((double)x + w, (double)y + h, (double)zLevel).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos((double)x + w, (double)y, (double)zLevel).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos((double)x, (double)y, (double)zLevel).color(f4, f5, f6, f7).endVertex();
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
        bufferbuilder.pos(x - 2, y+3, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + 2, y+3, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x - 2, y+12, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + 2, y+12, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCompleteImage(float posX, float posY, int width, int height) {
        glPushMatrix();
        glTranslatef(posX, posY, 0.0f);
        glBegin(7);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 0.0f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(0.0f, (float) height, 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f((float) width, (float) height, 0.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f((float) width, 0.0f, 0.0f);
        glEnd();
        glPopMatrix();
    }

    public static void drawCheckMark(float x, float y, int width, int color) {
        float f = (color >> 24 & 255) / 255.0f;
        float f1 = (color >> 16 & 255) / 255.0f;
        float f2 = (color >> 8 & 255) / 255.0f;
        float f3 = (color & 255) / 255.0f;
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(3553);
        glEnable(2848);
        glBlendFunc(770, 771);
        glLineWidth(4f);
        glBegin(3);
        glColor4f(0, 0, 0, 1.f);
        glVertex2d(x + width - 6.25, y + 2.75f);
        glVertex2d(x + width - 11.5, y + 10.25f);
        glVertex2d(x + width - 13.75f, y + 7.75f);
        glEnd();
        glLineWidth(1.5f);
        glBegin(3);
        glColor4f(f1, f2, f3, f);
        glVertex2d(x + width - 6.5, y + 3);
        glVertex2d(x + width - 11.5, y + 10);
        glVertex2d(x + width - 13.5, y + 8);
        glEnd();
        glEnable(3553);
        glDisable(GL_BLEND);
        glPopMatrix();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static double distance(Point one, Point two){
        double ac = abs(two.getY() - one.getY());
        double cb = abs(two.getX() - one.getX());

        return sqrt(ac * ac + cb * cb);
    }
}
