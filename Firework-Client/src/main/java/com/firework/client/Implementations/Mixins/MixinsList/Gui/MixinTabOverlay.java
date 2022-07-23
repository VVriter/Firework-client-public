package com.firework.client.Implementations.Mixins.MixinsList.Gui;

import com.firework.client.Features.Modules.Render.NoRender;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value={GuiPlayerTabOverlay.class})
public abstract class MixinTabOverlay {
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawRect(IIIII)V", ordinal = 2))
    private void onDrawInfoBoxColourPreClientRect(int left, int top, int right, int bottom, int color) {
        int colour = Color.CYAN.getRGB();
        if (colour != -1) RenderUtils2D.drawRect(left, top, right, bottom, colour);
    }
}