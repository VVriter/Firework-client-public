package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.RenderEntityModelEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import ua.firework.beet.Event;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ModuleManifest(name = "ChamsRewrite", category = Module.Category.COMBAT)
public class ChamsRewrite extends Module {

    public Setting<Boolean> players = new Setting<>("Players", false, this);
    public Setting<Boolean> mobs = new Setting<>("Mobs", false, this);
    public Setting<Boolean> crystals = new Setting<>("Crystals", true, this);

    public Setting<Boolean> outline = new Setting<>("Outline", true, this);
    public Setting<HSLColor> outlineColor = new Setting<>("OutlineColor", new HSLColor(178, 50, 50), this).setVisibility(v-> outline.getValue());
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 1, this, 1, 10).setVisibility(v-> outline.getValue());

    public Setting<Boolean> fill = new Setting<>("Fill", true, this);
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(34, 50, 50), this);

    @Subscribe
    public Listener<RenderEntityModelEvent> onEntityModelRender = new Listener<>(renderEntityModelEvent -> {
        if(!isValid(renderEntityModelEvent.getEntity())) return;

        final ModelBase modelBase = renderEntityModelEvent.getModelBase();
        final EntityLivingBase entityLivingBase = (EntityLivingBase) renderEntityModelEvent.getEntity();

        final float limbSwing = renderEntityModelEvent.getLimbSwing();
        final float limbSwingAmount = renderEntityModelEvent.getLimbSwingAmount();
        final float ageInTicks = renderEntityModelEvent.getAgeInTicks();
        final float netHeadYaw = renderEntityModelEvent.getNetHeadYaw();
        final float headPitch = renderEntityModelEvent.getHeadPitch();
        final float scaleFactor = renderEntityModelEvent.getScaleFactor();

        if(renderEntityModelEvent.getStage() == Event.Stage.INIT){
            if(outline.getValue()) {

            }
            if(fill.getValue()) {

            }
            float red = Color.red.getRed() / 255;
            float green = Color.red.getGreen() / 255;
            float blue = Color.red.getBlue() / 255;
            float alpha = Color.red.getAlpha() / 255;

            float red1 = Color.green.getRed() / 255;
            float green1 = Color.green.getGreen() / 255;
            float blue1 = Color.green.getBlue() / 255;
            float alpha1 = Color.green.getAlpha() / 255;

            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(red, green, blue, alpha);
            GL11.glLineWidth((float) 3);
            modelBase.render(entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glPopAttrib();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

            GL11.glColor4f(red1, green1, blue1, alpha1);
            modelBase.render(entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            GL11.glDisable(GL11.GL_LINE_SMOOTH);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    });

    public boolean isValid(Entity entity){
        if(entity instanceof EntityPlayer && players.getValue())
            return true;
        if(entity instanceof EntityCreature && mobs.getValue())
            return true;
        if(entity instanceof EntityEnderCrystal && crystals.getValue())
            return true;

        return false;
    }
}
