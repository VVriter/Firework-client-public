package com.firework.client.Implementations.Utill.Render;

import com.firework.client.Firework;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;
import static java.awt.Color.*;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

public class RenderUtils2D {

    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();

    public static void drawRect(int left, int top, int right, int bottom, int color) {
        int f3;

        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }

        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }

        float f31 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f31);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;

            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    public static void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;

            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }
    public static void drawGradientLine(Point2D.Double one, Point2D.Double two, Color startColor, Color endColor, int lineWidth)
    {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(lineWidth);
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(one.getX(), one.getY(), 0.0D).color(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), startColor.getAlpha()).endVertex();
        bufferbuilder.pos(two.getX(), two.getY(), 0.0D).color(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), endColor.getAlpha()).endVertex();

        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void renderItemStack(ItemStack itemStack, Point pos) {
        renderItemStack(itemStack, pos, null);
    }

    public static void renderItemStack(ItemStack itemStack, Point pos, String text) {
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        glPushAttrib(GL_SCISSOR_BIT);
        glDisable(GL_SCISSOR_TEST);
        GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
        glPopAttrib();
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, pos.x, pos.y);
        renderItemOverlayIntoGUI(itemStack, pos.x, pos.y, text);
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.popMatrix();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
    }

    public static void renderItemOverlayIntoGUI(ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
        if (!stack.isEmpty()){
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? String.valueOf(stack.getCount()) : text;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                Firework.customFontManagerInv.drawStringWithShadow(s, (float)(xPosition + 19 - 2 - Firework.customFontManagerInv.getWidth(s)), (float)(yPosition + 7), 16777215);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
            }

            if (stack.getItem().showDurabilityBar(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
                int i = Math.round(13.0F - (float)health * 13.0F);
                int j = rgbfordisplay;
                RenderUtils2D.drawRectangle(new Rectangle(xPosition + 2, yPosition + 13, 13, 2), new Color(0, 0, 0));
                RenderUtils2D.drawRectangle(new Rectangle(xPosition + 2, yPosition + 13, i, 1), new Color(j >> 16 & 255, j >> 8 & 255, j & 255));
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }

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

        double x = rectangle.x;
        double y = rectangle.y;
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

        double x = rectangle.x;
        double y = rectangle.y;
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

    public static void drawRectAlpha(Rectangle rectangle, Color color)
    {
        double zLevel=0.0;

        float f = (float) color.getRed() / 255.0f;
        float f1 = (float) color.getGreen() / 255.0f;
        float f2 = (float) color.getBlue() / 255.0f;
        float f3 = (float) color.getAlpha() / 255.0f;

        double x = rectangle.x;
        double y = rectangle.y;
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
        bufferbuilder.pos((double)x + w, (double)y, (double)zLevel).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos((double)x, (double)y, (double)zLevel).color(f, f1, f2, f3).endVertex();
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

        double x = rectangle.x;
        double y = rectangle.y;
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

    public static void drawRectangleOutlineLinesMode(Rectangle rectangle, float width, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(width);
        double x = rectangle.x;
        double y = rectangle.y;
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

    public static void drawRectangleOutline(Rectangle rectangle, float width, Color color) {
        double x = rectangle.x;
        double y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;

        //Left side
        drawRectangle(new Rectangle(x, y+width, width, h-width), color);
        //Right side
        drawRectangle(new Rectangle(x + w - width, y+width, width, h-width), color);
        //Up side
        drawRectangle(new Rectangle(x, y, w, width), color);
        //Down side
        drawRectangle(new Rectangle(x, y + h - width, w, width), color);
    }

    public static void drawGradientRectangleOutline(Rectangle rectangle, float width, Color color1, Color color2) {
        double x = rectangle.x;
        double y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;

        //Left side
        drawGradientRectVertical(new Rectangle(x, y+width, width, h-width), color2, color1);
        //Right side
        drawGradientRectVertical(new Rectangle(x + w - width, y+width, width, h-width), color2, color1);
        //Up side
        drawRectangle(new Rectangle(x, y, w, width), color1);
        //Down side
        drawRectangle(new Rectangle(x, y + h, w, width), color2);
    }

    public static void drawGradientTriangle(ArrayList<Point> points, ArrayList<Color> colors)
    {
        double zLevel=0.0;

        float f = (float) colors.get(0).getRed() / 255.0f;
        float f1 = (float) colors.get(0).getGreen() / 255.0f;
        float f2 = (float) colors.get(0).getBlue() / 255.0f;
        float f3 = (float) colors.get(0).getAlpha() / 255.0f;
        float f4 = (float) colors.get(1).getRed() / 255.0f;
        float f5 = (float) colors.get(1).getGreen() / 255.0f;
        float f6 = (float) colors.get(1).getBlue() / 255.0f;
        float f7 = (float) colors.get(1).getAlpha() / 255.0f;
        float f8 = (float) colors.get(2).getRed() / 255.0f;
        float f9 = (float) colors.get(2).getGreen() / 255.0f;
        float f10 = (float) colors.get(2).getBlue() / 255.0f;
        float f11 = (float) colors.get(2).getAlpha() / 255.0f;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)points.get(0).x, (double)points.get(0).y, (double)zLevel).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos((double)points.get(1).x, (double)points.get(1).y, (double)zLevel).color(f4, f5, f6, f7).endVertex();
        bufferbuilder.pos((double)points.get(2).x, (double)points.get(2).y, (double)zLevel).color(f8, f9, f10, f11).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawCircleOutline(Point2D.Double o, float radius, float width, Color color) {
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

    public static void drawFilledCircle(Point2D.Double point, Color color, int r)
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

    public static void drawColorPickerBaseV2(Point point, HSLColor hslColor, int r)
    {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        bufferbuilder.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        bufferbuilder.pos(point.getX(), point.getY(), 0.0D).color(white.getRed(), white.getGreen(), white.getBlue(), white.getAlpha()).endVertex();

        for (int i = 0; i <= 360; i++)
        {
            double x = Math.sin(((i * Math.PI) / 180)) * r;
            double y = Math.cos(((i * Math.PI) / 180)) * r;

            double x2 = Math.sin(((i * Math.PI) / 180)) * (r-5);
            double y2 = Math.cos(((i * Math.PI) / 180)) * (r-5);

            HSLColor color = new HSLColor(i + 1, hslColor.saturation, hslColor.light);
            Color colorRGB = color.toRGB();

            bufferbuilder.pos(point.getX() + x, point.getY() + y, 0.0D).color(colorRGB.getRed(), colorRGB.getGreen(), colorRGB.getBlue(), colorRGB.getAlpha()).endVertex();
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

        double x = rectangle.x;
        double y = rectangle.y;
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

    public static void drawCheckMarkV2(Point2D.Double point, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(1);

        double x = point.x;
        double y = point.y;

        bufferbuilder.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x + 3, y-4.6, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x + 1.4, y-5.3, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x, y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x, y-2, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x-2, y-1, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferbuilder.pos(x-2, y-2.6, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawArc(Point2D.Double point, double r, int start_angle, int end_angle, Color color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        bufferbuilder.begin(GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

        bufferbuilder.pos(point.getX(), point.getY(), 0.0D).color(white.getRed(), white.getGreen(), white.getBlue(), white.getAlpha()).endVertex();

        for (int i = start_angle; i <= end_angle; i++)
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

    public static void drawRoundedRectangle(Rectangle rectangle, float radius, Color color) {

        double x = rectangle.x;
        double y = rectangle.y;
        double width = rectangle.width;
        double height = rectangle.height;

        Point2D.Double leftTop = new Point2D.Double(x + radius, y + radius);
        drawArc(leftTop, radius, 180, 270, color);

        Point2D.Double leftDown = new Point2D.Double(x + radius, y + radius);
        drawArc(leftDown, radius, 270, 360, color);

        Point2D.Double rightTop = new Point2D.Double(x + width - radius, y + radius);
        drawArc(rightTop, radius, 90, 180, color);

        Point2D.Double rightDown = new Point2D.Double(x + width - radius, y + radius);
        drawArc(rightDown, radius, 0, 90, color);

        Rectangle middleRect = new Rectangle(x + radius, y, width - 2*radius, height);
        drawRectangle(middleRect, color);

        Rectangle leftRect = new Rectangle(x, y - radius, radius, height-2*radius);
        drawRectangle(leftRect, color);

        Rectangle right = new Rectangle(x + width - radius, y - radius, radius, height-2*radius);
        drawRectangle(right, color);
    }

    public static void drawCheckMarkV3(Rectangle rectangle, boolean enabled, double width){
        int radius = (int) (rectangle.height/2);
        drawRoundedRectangle(rectangle, radius, enabled ? colorManager.getColor() : gray);
        Point2D.Double circleMarkPoint = null;
        circleMarkPoint = new Point2D.Double(rectangle.x + width,rectangle.y + radius);
        drawFilledCircle(circleMarkPoint, white, radius);
    }

    public static void drawCheckBoxV1(Rectangle rectangle, boolean enabled){
        double x = rectangle.x;
        double y = rectangle.y;
        double width = rectangle.width;
        double height = rectangle.height;

        if(enabled) {
            drawRectangle(new Rectangle(x + width * 1/4, y, width * 3/4, height), new Color(RainbowUtil.astolfoColors(100, 100)));
            customFontManager.drawString("ON", (float) (x + width * 1/4 + (width * 3/4 - customFontManager.getWidth("ON"))/2), (float) ((float)y + (height - customFontManager.getHeight())/2 + 1), white.getRGB());
        }else {
            drawRectangle(new Rectangle(x, y, width * 3/4, height), white);
            customFontManager.drawString("OFF", (float) (x + (width * 3/4 - customFontManager.getWidth("OFF"))/2), (float) ((float)y + (height - customFontManager.getHeight())/2 + 1), RainbowUtil.astolfoColors(100, 100));
        }

        drawRectangleOutline(rectangle, 0.5f, white);
    }

    public static void drawHueBar(Rectangle rectangle){
        double x = rectangle.x;
        double y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;
        for(int i = 0; i < 360; i++){
            Color color = new HSLColor(i, 50, 50).toRGB();
            drawRectangle(new Rectangle(x + (i*w/360), y, w/360,h), color);
        }
    }

    public static void drawAlphaBarBase(Rectangle rectangle){
        double x = rectangle.x;
        double y = rectangle.y;
        double w = rectangle.width;
        double h = rectangle.height;

        for(int b = 0; b < (w); b += 2) {
            for (int i = 0; i < (h); i += 2) {
                Color activeColor;
                if ((i + b )/2 % 2 == 0)
                    activeColor = white;
                else
                    activeColor = gray;
                drawRectangle(new Rectangle(x + b, y + i, 2, 2), activeColor);
            }
        }
    }

    public static void drawColorPickerBase(Rectangle rectangle, Color color){
        drawGradientRectHorizontal(rectangle, color, white);
        drawGradientRectVertical(rectangle, black, new Color(1, 1, 1, 0));
    }

    public static double distance(Point one, Point two){
        double ac = abs(two.getY() - one.getY());
        double cb = abs(two.getX() - one.getX());

        return sqrt(ac * ac + cb * cb);
    }

    public static double distance(Point2D.Double one, Point2D.Double two){
        double ac = abs(two.getY() - one.getY());
        double cb = abs(two.getX() - one.getX());

        return sqrt(ac * ac + cb * cb);
    }
}
