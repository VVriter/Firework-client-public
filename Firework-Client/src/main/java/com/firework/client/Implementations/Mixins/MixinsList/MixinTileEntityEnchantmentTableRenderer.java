package com.firework.client.Implementations.Mixins.MixinsList;

import com.firework.client.Features.Modules.Movement.BoatFlyRewrote;
import com.firework.client.Features.Modules.Render.NoRender;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TileEntityEnchantmentTableRenderer.class})
public abstract class MixinTileEntityEnchantmentTableRenderer {
    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    public void fov2(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.enchatntTable.getValue()) {
            info.cancel();
        }
    }
}