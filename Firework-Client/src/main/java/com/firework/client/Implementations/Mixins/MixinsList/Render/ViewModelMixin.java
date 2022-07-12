package com.firework.client.Implementations.Mixins.MixinsList.Render;

import com.firework.client.Features.Modules.Render.ItemViewModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.firework.client.Implementations.Utill.Util.mc;

@Mixin(ItemRenderer.class)
public class ViewModelMixin {

    @Inject(method = "renderItemSide", at = @At("HEAD"))
    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (ItemViewModel.enabled.getValue() && mc.player != null && mc.world != null) {
            GlStateManager.scale(ItemViewModel.scaleX.getValue() / 100F, ItemViewModel.scaleY.getValue() / 100F, ItemViewModel.scaleZ.getValue() / 100F);
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                if (ItemViewModel.noEat.getValue()) {
                if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemFood && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    GlStateManager.translate(0, 0, 0);
                } else {
                     GlStateManager.translate(ItemViewModel.translateX.getValue() / 100F, ItemViewModel.translateY.getValue() / 100F, ItemViewModel.translateZ.getValue() / 100F);
                }
                } else {
                    GlStateManager.translate(ItemViewModel.translateX.getValue() / 100F, ItemViewModel.translateY.getValue() / 100F, ItemViewModel.translateZ.getValue() / 100F);
                }
                GlStateManager.rotate(ItemViewModel.rotateXR.getValue().floatValue(), 1, 0, 0);
                GlStateManager.rotate(ItemViewModel.rotateYR.getValue().floatValue(), 0, 1, 0);
                GlStateManager.rotate(ItemViewModel.rotateZR.getValue().floatValue(), 0, 0, 1);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate(-ItemViewModel.translateX.getValue() / 100F, ItemViewModel.translateY.getValue() / 100F, ItemViewModel.translateZ.getValue() / 100F);
                GlStateManager.rotate(-ItemViewModel.rotateXL.getValue().floatValue(), 1, 0, 0);
                GlStateManager.rotate(ItemViewModel.rotateYL.getValue().floatValue(), 0, 1, 0);
                GlStateManager.rotate(ItemViewModel.rotateZL.getValue().floatValue(), 0, 0, 1);
            }
        }
    }
}