package com.firework.client.Implementations.Mixins.MixinsList;


import com.firework.client.Features.Modules.Movement.BoatFlyRewrote;
import com.firework.client.Features.Modules.Render.NoRender;
import net.minecraft.client.renderer.entity.RenderBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value={RenderBoat.class})
public abstract class MixinRenderBoat {
    @Inject(method={"doRender"}, at={@At(value="HEAD")}, cancellable=true)
    public void fov2(CallbackInfo info) {
        if (BoatFlyRewrote.enabled.getValue() && BoatFlyRewrote.noRenderBoat.getValue()) {
            info.cancel();
        }
    }
}