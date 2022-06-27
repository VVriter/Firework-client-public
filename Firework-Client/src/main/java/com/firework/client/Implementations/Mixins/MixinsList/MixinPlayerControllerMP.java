package com.firework.client.Implementations.Mixins.MixinsList;

import com.firework.client.Features.Modules.Misc.PacketUse;

import com.firework.client.Features.Modules.Misc.Reach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {


    @Shadow
    public abstract void syncCurrentPlayItem();


    @Inject(method={"getBlockReachDistance"}, at={@At(value="RETURN")}, cancellable=true)
    private void getReachDistanceHook(CallbackInfoReturnable<Float> distance) {
        if (Reach.enabled.getValue()) {
            float range = distance.getReturnValue().floatValue();
            distance.setReturnValue(Reach.override.getValue() != false ? Reach.reach.getValue().floatValue() : range + Reach.add.getValue().floatValue());
        }
    }
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