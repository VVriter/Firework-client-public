package com.firework.client.Implementations.Mixins.MixinsList.Else;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Movement.PushedByShulkerEvent;
import net.minecraft.tileentity.TileEntityShulkerBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityShulkerBox.class)
public abstract class MixinTileEntityShulkerBox {

    @Inject(
            method = "moveCollidedEntities",
            at = @At("HEAD"),
            cancellable = true)
    private void moveCollidedEntitiesHook(CallbackInfo info)
    {
        PushedByShulkerEvent event = new PushedByShulkerEvent();
        Firework.eventBus.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

}