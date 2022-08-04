package com.firework.client.Implementations.Mixins.MixinsList.Gui;

import com.firework.client.Features.Modules.Render.NoRender;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay {

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    public void renderBossHealth(CallbackInfo ci) {
        if (NoRender.enabled.getValue() && NoRender.bosssOverlay.getValue()) {
            ci.cancel();
        }
    }

}