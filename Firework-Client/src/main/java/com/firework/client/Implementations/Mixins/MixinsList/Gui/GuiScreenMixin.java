package com.firework.client.Implementations.Mixins.MixinsList.Gui;

import com.firework.client.Features.Modules.Render.NoRender;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreen.class})
public abstract class GuiScreenMixin {
    @Inject(method={"drawDefaultBackground"}, at={@At(value="HEAD")}, cancellable=true)
    public void fov2(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.defaultBackground.getValue()) {
            info.cancel();
        }
    }
}