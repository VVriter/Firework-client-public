package com.firework.client.Implementations.Mixins.MixinsList.Misc;

import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo callbackInfo) {
        UpdateWalkingPlayerEvent eventPlayerMotionUpdate = new UpdateWalkingPlayerEvent(0);
        MinecraftForge.EVENT_BUS.post(eventPlayerMotionUpdate);
        if (eventPlayerMotionUpdate.isCanceled())
            callbackInfo.cancel();
    }
}
