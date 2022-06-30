package com.firework.client.Implementations.Mixins.MixinsList;

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
        MinecraftForge.EVENT_BUS.post(event);
    }
}
