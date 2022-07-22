package com.firework.client.Implementations.Mixins.MixinsList.Misc;

import com.firework.client.Features.Modules.Combat.Surround;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PlayerUpdateEvent;
import com.firework.client.Implementations.Events.TestEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.firework.beet.Event;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo callbackInfo) {
        UpdateWalkingPlayerEvent eventPlayerMotionUpdate = new UpdateWalkingPlayerEvent(Event.Stage.PRE);
        Surround.time = System.nanoTime();
        Firework.eventBus.post(eventPlayerMotionUpdate);
        if (eventPlayerMotionUpdate.isCancelled())
            callbackInfo.cancel();
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onPlayerUpdate(CallbackInfo info) {
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        MinecraftForge.EVENT_BUS.post(playerUpdateEvent);
    }
}
