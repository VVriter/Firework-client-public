package com.firework.client.Implementations.Mixins.MixinsList;

import com.firework.client.Features.Modules.Misc.PacketUse;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {


    @Shadow
    public abstract void syncCurrentPlayItem();

    @Inject(method = "onStoppedUsingItem", at = @At("HEAD"), cancellable = true)
    public void onStoppedUsingItem(EntityPlayer playerIn, CallbackInfo ci) {
        if (PacketUse.enabled.getValue()) {
            if ((PacketUse.food.getValue() && playerIn.getHeldItem(playerIn.getActiveHand()).getItem() instanceof ItemFood)
                    || (PacketUse.potion.getValue() && playerIn.getHeldItem(playerIn.getActiveHand()).getItem() instanceof ItemPotion)
                    || PacketUse.all.getValue()) {
                this.syncCurrentPlayItem();
                playerIn.stopActiveHand();
                ci.cancel();
            }
        }
    }
}