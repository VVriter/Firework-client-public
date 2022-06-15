package com.firework.client.Implementations.GuiNEO.Particles;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

import static com.firework.client.Implementations.Utill.Render.RenderUtils2D.bufferbuilder;
import static com.firework.client.Implementations.Utill.Render.RenderUtils2D.tessellator;

public class RenderSystem {

    public static void drawGradientLine(Point one, Point two, Color startColor, Color endColor)
    {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(5);
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

}
