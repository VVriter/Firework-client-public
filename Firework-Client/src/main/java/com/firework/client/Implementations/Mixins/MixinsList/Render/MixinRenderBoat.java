package com.firework.client.Implementations.Mixins.MixinsList.Render;


import net.minecraft.client.renderer.entity.RenderBoat;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(value={RenderBoat.class})
public abstract class MixinRenderBoat {
/*    @Inject(method={"doRender"}, at={@At(value="HEAD")}, cancellable=true)
    public void fov2(CallbackInfo info) {
        if (BoatFly.enabled.getValue() && BoatFly.noRenderBoat.getValue()) {
            info.cancel();
        }
    } */
}