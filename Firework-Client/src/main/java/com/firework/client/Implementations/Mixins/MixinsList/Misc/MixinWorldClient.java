package com.firework.client.Implementations.Mixins.MixinsList.Misc;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.WorldClientInitEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class MixinWorldClient {

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onWorldClientInit(CallbackInfo callbackInfo) {
        WorldClientInitEvent event = new WorldClientInitEvent();
        Firework.eventBus.post(event);
    }
}
