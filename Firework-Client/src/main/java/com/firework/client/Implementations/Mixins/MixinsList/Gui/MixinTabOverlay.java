package com.firework.client.Implementations.Mixins.MixinsList.Gui;

import com.firework.client.Features.Modules.Render.BetterTabOverlay;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(value={GuiPlayerTabOverlay.class})
public abstract class MixinTabOverlay {
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawRect(IIIII)V", ordinal = 2))
    private void onDrawInfoBoxColourPreClientRect(int left, int top, int right, int bottom, int color) {
        if (BetterTabOverlay.enabled.getValue()) {
            RenderUtils2D.drawRect(left, top, right, bottom, BetterTabOverlay.fillColorNickNames.getValue().toRGB().getRGB());
        }
    }

    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawRect(IIIII)V", ordinal = 3))
    private void onRedirectFooterRect(int left, int top, int right, int bottom, int color) {
        if (BetterTabOverlay.enabled.getValue()) {
            RenderUtils2D.drawRect(left, top, right, bottom, BetterTabOverlay.fillColorBorder.getValue().toRGB().getRGB());
        }
    }
}