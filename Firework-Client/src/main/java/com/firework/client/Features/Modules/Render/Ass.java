package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import net.minecraft.client.renderer.entity.*;
import org.lwjgl.util.glu.*;

@ModuleManifest(name = "Ass",category = Module.Category.RENDER)
public class Ass extends Module {

    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 54, 43), this);


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent renderPartialTicks) {
        for (final Object e : mc.world.loadedEntityList) {
            if (!(e instanceof EntityPlayer)) {
                continue;
            }
            final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
            final EntityPlayer entityPlayer = (EntityPlayer)e;
            final double d = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX);
            final double d2 = d - renderManager.viewerPosX;
            final double d3 = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) ;
            final double d4 = d3 - renderManager.viewerPosY;
            final double d5 = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ);
            final double d6 = d5 - renderManager.viewerPosZ;
            GL11.glPushMatrix();
            RenderHelper.disableStandardItemLighting();
            this.esp(entityPlayer, d2, d4, d6);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
        }
    }

    public void esp(final EntityPlayer entityPlayer, final double d, final double d2, final double d3) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(d, d2, d3);
        GL11.glRotatef(-entityPlayer.rotationYaw, 0.0f, entityPlayer.height, 0.0f);
        GL11.glTranslated(-d, -d2, -d3);
        GL11.glTranslated(d, d2 + entityPlayer.height / 2.0f - 0.22499999403953552, d3);
        GL11.glColor4f(1,200,1,200);
        GL11.glRotated((double)(entityPlayer.isSneaking() ? 35 : 0), 1.0, 0.0, 0.0);
        final Sphere sphere = new Sphere();
        GL11.glTranslated(-0.15, 0.0, -0.2);
        sphere.setDrawStyle(100013);
        sphere.draw(0.2f, 10, 20);
        GL11.glTranslated(0.3500000014901161, 0.0, 0.0);
        final Sphere sphere2 = new Sphere();
        sphere2.setDrawStyle(100013);
        sphere2.draw(0.2f, 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }

}
