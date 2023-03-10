package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.RenderEntityModelEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import ua.firework.beet.Event;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;

@ModuleManifest(name = "Chams", category = Module.Category.VISUALS)
public class Chams extends Module {

    public Setting<Boolean> players = new Setting<>("Players", false, this);
   // public Setting<Boolean> autoAlpha = new Setting<>("AutoAlpha", false, this).setVisibility(v-> players.getValue());
    public Setting<Boolean> mobs = new Setting<>("Mobs", false, this);
    public Setting<Boolean> crystals = new Setting<>("Crystals", true, this);

    public Setting<Boolean> outline = new Setting<>("Outline", true, this);
    public Setting<HSLColor> outlineColor = new Setting<>("OutlineColor", new HSLColor(178, 50, 50), this).setVisibility(()-> outline.getValue());
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 1, this, 1, 10).setVisibility(()-> outline.getValue());

    public Setting<Boolean> fill = new Setting<>("Fill", true, this);
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(34, 50, 50), this);

    @Subscribe
    public Listener<RenderEntityModelEvent> onEntityModelRender = new Listener<>(renderEntityModelEvent -> {
        if(fullNullCheck()) return;
        if(!isValid(renderEntityModelEvent.getEntity())) return;

        if(renderEntityModelEvent.getStage() == Event.Stage.PRE){
            renderEntityModelEvent.setCancelled(true);
            if(fill.getValue()) {

                mc.getRenderManager().setRenderShadow(false);
                mc.getRenderManager().setRenderOutlines(false);
                GlStateManager.pushMatrix();
                GlStateManager.depthMask(true);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                glEnable(GL_POLYGON_OFFSET_FILL);
                glDepthRange(0.0, 0.01);
                glDisable(GL_TEXTURE_2D);
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
                GlStateManager.color(fillColor.getValue().toRGB().getRed() / 255.0f,
                        fillColor.getValue().toRGB().getGreen() / 255.0f,
                        fillColor.getValue().toRGB().getBlue() / 255.0f,
                        fillColor.getValue().toRGB().getAlpha() / 255.0f);

                GlStateManager.popMatrix();
                renderEntityModelEvent.render();
                GlStateManager.pushMatrix();

                boolean shadow = mc.getRenderManager().isRenderShadow();
                mc.getRenderManager().setRenderShadow(shadow);
                GlStateManager.depthMask(false);
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
                glDisable(GL_POLYGON_OFFSET_FILL);
                glDepthRange(0.0, 1.0);
                glEnable(GL_TEXTURE_2D);
                GlStateManager.popMatrix();
            }
            if(outline.getValue()) {

                mc.getRenderManager().setRenderShadow(false);
                mc.getRenderManager().setRenderOutlines(false);
                GlStateManager.pushMatrix();
                GlStateManager.depthMask(true);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                glEnable(GL_POLYGON_OFFSET_LINE);
                glDepthRange(0.0, 0.01);
                glDisable(GL_TEXTURE_2D);
                glDisable(GL_LIGHTING);
                glEnable(GL_LINE_SMOOTH);
                glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
                DecimalFormat df = new DecimalFormat("#.####");
                float scaleFactor = Float.parseFloat(df.format((128 - renderEntityModelEvent.getEntity().getPositionVector().distanceTo(mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0)))/128));
                if(scaleFactor <= 0)
                    scaleFactor = 0.0001f;
                glLineWidth(outlineWidth.getValue() * scaleFactor);
                GlStateManager.color(outlineColor.getValue().toRGB().getRed() / 255.0f,
                        outlineColor.getValue().toRGB().getGreen() / 255.0f,
                        outlineColor.getValue().toRGB().getBlue() / 255.0f,
                        outlineColor.getValue().toRGB().getAlpha() / 255.0f);
                GlStateManager.popMatrix();
                renderEntityModelEvent.render();
                GlStateManager.pushMatrix();
                boolean shadow = mc.getRenderManager().isRenderShadow();
                mc.getRenderManager().setRenderShadow(shadow);
                GlStateManager.depthMask(false);
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                glDisable(GL_POLYGON_OFFSET_LINE);
                glDepthRange(0.0, 1.0);
                glEnable(GL_TEXTURE_2D);
                glEnable(GL_LIGHTING);
                glDisable(GL_LINE_SMOOTH);
                GlStateManager.popMatrix();
            }
        }
    });

 /*   EntityPlayer closestTarget;
    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> evv = new Listener<>(e-> {
        closestTarget = PlayerUtil.getClosestTarget();
        if (EntityUtil.getDistanceFromEntityToEntity(closestTarget) <= 3) {
            fillColor.setValue(new HSLColor(fillColor.getValue().hue, fillColor.getValue().saturation,fillColor.getValue().light, 10));
        } else {
            fillColor.setValue(new HSLColor(fillColor.getValue().hue, fillColor.getValue().saturation,fillColor.getValue().light, 100));
        }
    }); */

    public boolean isValid(Entity entity){
        if(entity == mc.player)
            return false;
        if(entity instanceof EntityPlayer && players.getValue())
            return true;
        if(entity instanceof EntityCreature && mobs.getValue())
            return true;
        if(entity instanceof EntityEnderCrystal && crystals.getValue())
            return true;

        return false;
    }
}
