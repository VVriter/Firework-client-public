package com.firework.client.Implementations.Utill.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

/*
    @author PunCakeCat
 */
public class EntityRenderBuilder2D {
    private EntityLivingBase entityLivingBase;

    private float rotationYaw = 0, rotationPitch = 0, rotationYawHead = 0, renderYawOffset = 0;
    private double posX = 0, posY = 0;
    private float scale = 1;

    public EntityRenderBuilder2D(EntityLivingBase entityLivingBase){
        this.entityLivingBase = entityLivingBase;
        this.entityLivingBase.setCustomNameTag("");
    }

    public EntityRenderBuilder2D setPosition(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
        return this;
    }

    public EntityRenderBuilder2D setRotation(float renderYawOffset, float rotationYaw, float rotationPitch){
        this.renderYawOffset = renderYawOffset;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        return this;
    }

    public EntityRenderBuilder2D setRotationToCursor(int mouseX, int mouseY){
        this.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        this.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        this.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        return this;
    }

    public EntityRenderBuilder2D setHeadRotation(float rotationYawHead){
        this.rotationYawHead = rotationYawHead;
        return this;
    }

    public EntityRenderBuilder2D setScale(float scale){
        this.scale = scale;
        return this;
    }

    public EntityRenderBuilder2D render(){
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = entityLivingBase.renderYawOffset;
        float f1 = entityLivingBase.rotationYaw;
        float f2 = entityLivingBase.rotationPitch;
        float f3 = entityLivingBase.prevRotationYawHead;
        float f4 = entityLivingBase.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        entityLivingBase.renderYawOffset = renderYawOffset;
        entityLivingBase.rotationYaw = rotationYaw;
        entityLivingBase.rotationPitch = rotationPitch;
        entityLivingBase.rotationYawHead = rotationYawHead;
        entityLivingBase.prevRotationYawHead = rotationYawHead;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(entityLivingBase, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        entityLivingBase.renderYawOffset = f;
        entityLivingBase.rotationYaw = f1;
        entityLivingBase.rotationPitch = f2;
        entityLivingBase.prevRotationYawHead = f3;
        entityLivingBase.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        return this;
    }


}
