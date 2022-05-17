package com.firework.client.Implementations.Mixins.MixinsList;

import com.firework.client.Features.Modules.World.EntityControl;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value={AbstractHorse.class})
public class MixinAbstractHorse {
    @Inject(method={"isHorseSaddled"}, at={@At(value="HEAD")}, cancellable=true)
    public void isHorseSaddled(CallbackInfoReturnable<Boolean> cir) {
        if (EntityControl.INSTANCE.saddle.getValue() && EntityControl.INSTANCE.isEnabled.getValue()) {
            cir.setReturnValue(true);
        }
    }
}