package com.firework.client.Implementations.Events.Render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import ua.firework.beet.Event;

public class RenderEntityModelEvent extends Event {
    private final Entity entity;
    private final ModelBase modelBase;

    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float netHeadYaw;
    private float headPitch;
    private float scaleFactor;

    public RenderEntityModelEvent(Stage stage, Entity entity, ModelBase modelBase,
                                  float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                                  float headPitch, float scaleFactor){
        super(stage);
        this.entity = entity;
        this.modelBase = modelBase;

        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleFactor = scaleFactor;
    }

    public Entity getEntity(){
        return this.entity;
    }

    public ModelBase getModelBase(){
        return this.modelBase;
    }

    public float getLimbSwing() {
        return limbSwing;
    }

    public void setLimbSwing(float limbSwing) {
        this.limbSwing = limbSwing;
    }

    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    public void setLimbSwingAmount(float limbSwingAmount) {
        this.limbSwingAmount = limbSwingAmount;
    }

    public float getAgeInTicks() {
        return ageInTicks;
    }

    public void setAgeInTicks(float ageInTicks) {
        this.ageInTicks = ageInTicks;
    }

    public float getNetHeadYaw() {
        return netHeadYaw;
    }

    public void setNetHeadYaw(float netHeadYaw) {
        this.netHeadYaw = netHeadYaw;
    }

    public float getHeadPitch() {
        return headPitch;
    }

    public void setHeadPitch(float headPitch) {
        this.headPitch = headPitch;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
