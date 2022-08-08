package com.firework.client.Implementations.Mixins.MixinsList.Render;

import com.firework.client.Features.Modules.Render.ItemModel;
import com.firework.client.Features.Modules.Render.ItemModel;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.UpdateEquippedItemEvent;
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
        if (ItemModel.enabled.getValue() && mc.player != null && mc.world != null) {
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.scale(ItemModel.RSX.getValue() / 100F, ItemModel.RSY.getValue() / 100F, ItemModel.RSZ.getValue() / 100F);
                if (ItemModel.noEat.getValue()) {
                if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemFood && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    GlStateManager.translate(0, 0, 0);
                } else {
                     GlStateManager.translate(ItemModel.TRX.getValue() / 100F, ItemModel.TRY.getValue() / 100F, ItemModel.TRZ.getValue() / 100F);
                }
                } else {
                    GlStateManager.translate(ItemModel.TRX.getValue() / 100F, ItemModel.TRY.getValue() / 100F, ItemModel.TRZ.getValue() / 100F);
                }
                GlStateManager.rotate(ItemModel.RRX.getValue().floatValue(), 1, 0, 0);
                GlStateManager.rotate(ItemModel.RRY.getValue().floatValue(), 0, 1, 0);
                GlStateManager.rotate(ItemModel.RRZ.getValue().floatValue(), 0, 0, 1);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.scale(ItemModel.LSX.getValue() / 100F, ItemModel.LSY.getValue() / 100F, ItemModel.LSZ.getValue() / 100F);
                GlStateManager.translate(ItemModel.TLX.getValue() / 100F, ItemModel.TLY.getValue() / 100F, ItemModel.TLZ.getValue() / 100F);
                GlStateManager.rotate(-ItemModel.RLX.getValue().floatValue(), 1, 0, 0);
                GlStateManager.rotate(ItemModel.RLY.getValue().floatValue(), 0, 1, 0);
                GlStateManager.rotate(ItemModel.RLZ.getValue().floatValue(), 0, 0, 1);
            }
        }
    }

    @Inject(method = {"updateEquippedItem"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void onUpdateEquippedItem(CallbackInfo ci) {
        UpdateEquippedItemEvent event = new UpdateEquippedItemEvent();
        Firework.eventBus.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}