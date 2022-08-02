package com.firework.client.Implementations.Mixins.MixinsList.Render;

import com.firework.client.Features.Modules.Render.NoRender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={LayerBipedArmor.class})
public abstract class MixinLayerBipedArmor {
    @Shadow
    protected abstract void setModelVisible(ModelBiped var1);

    /**
     * @author dazed68
     * @reason nado
     */
    @Overwrite
    protected void setModelSlotVisible(ModelBiped p_188359_1_, EntityEquipmentSlot slotIn) {
        this.setModelVisible(p_188359_1_);
        switch (slotIn) {
            case HEAD: {
                p_188359_1_.bipedHead.showModel = !NoRender.enabled.getValue() || NoRender.helmet.getValue() == false;
                p_188359_1_.bipedHeadwear.showModel = !NoRender.enabled.getValue() || NoRender.helmet.getValue() == false;
                break;
            }
            case CHEST: {
                p_188359_1_.bipedBody.showModel = !NoRender.enabled.getValue() || NoRender.chestplate.getValue() == false;
                p_188359_1_.bipedRightArm.showModel = !NoRender.enabled.getValue() || NoRender.chestplate.getValue() == false;
                p_188359_1_.bipedLeftArm.showModel = !NoRender.enabled.getValue() || NoRender.chestplate.getValue() == false;
                break;
            }
            case LEGS: {
                p_188359_1_.bipedBody.showModel = !NoRender.enabled.getValue() || NoRender.leggings.getValue() == false;
                p_188359_1_.bipedRightLeg.showModel = !NoRender.enabled.getValue() || NoRender.leggings.getValue() == false;
                p_188359_1_.bipedLeftLeg.showModel = !NoRender.enabled.getValue() || NoRender.leggings.getValue() == false;
                break;
            }
            case FEET: {
                p_188359_1_.bipedRightLeg.showModel = !NoRender.enabled.getValue() || NoRender.boots.getValue() == false;
                p_188359_1_.bipedLeftLeg.showModel = !NoRender.enabled.getValue() || NoRender.boots.getValue() == false;
            }
        }
    }
}