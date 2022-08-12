package com.firework.client.Implementations.Utill.Render;

import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class RenderText {


    public static void drawTextByVec3d(Vec3d vec, String text, float y, float scale, int color) {
        GlStateManager.pushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        glBillboardDistanceScaled((float) vec.x, (float) (vec.y + y), (float) vec.z, mc.player, scale);
        GlStateManager.disableDepth();
        GlStateManager.translate(-((double) Firework.textManager.getStringWidth(text) / 2.0), 0.0, 0.0);
        RenderRound.drawRound(0.0f, 0.0f,100,6,1,true,Color.WHITE);
        Firework.textManager.drawStringWithShadow(text, 0.0f, 0.0f, color);
        GlStateManager.popMatrix();
    }

    public static void drawText(BlockPos pos, String text) {
        GlStateManager.pushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        glBillboardDistanceScaled((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f, mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-((double) Firework.textManager.getStringWidth(text) / 2.0), 0.0, 0.0);
        Firework.textManager.drawStringWithShadow(text, 0.0f, 0.0f, Color.WHITE.getRGB());
        GlStateManager.popMatrix();
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = (float) distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.translate((double) x - mc.getRenderManager().viewerPosX, (double) y - mc.getRenderManager().viewerPosY, (double) z - mc.getRenderManager().viewerPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.player.rotationPitch, mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }
}
