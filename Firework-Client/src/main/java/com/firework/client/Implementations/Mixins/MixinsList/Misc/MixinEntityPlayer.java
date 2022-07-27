package com.firework.client.Implementations.Mixins.MixinsList.Misc;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Movement.PlayerPushedByWaterEvent;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase {

    @Inject(method = "isPushedByWater()Z", at = @At("HEAD"), cancellable = true)
    public void isPushedByWater(CallbackInfoReturnable<Boolean> info) {
        PlayerPushedByWaterEvent event = new PlayerPushedByWaterEvent();
        Firework.eventBus.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}