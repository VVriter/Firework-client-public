package com.firework.client.Implementations.Mixins.MixinsList.Render;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.FireRendereEvent;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinPlayerOverlay {

    @Inject(method = "renderPotionEffects", at = @At("HEAD"), cancellable = true)
    protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo callbackInfo) {
        FireRendereEvent av = new FireRendereEvent();
        Firework.eventBus.post(av);
        if (av.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}