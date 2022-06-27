package com.firework.client.Implementations.Mixins.MixinsList;

import com.firework.client.Features.Modules.Render.ItemViewModel;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityLivingBase.class})
public class MixinEntityLivingBase {
    @Inject(method={"getArmSwingAnimationEnd"}, at={@At(value="HEAD")}, cancellable=true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        if (ItemViewModel.enabled.getValue() && ItemViewModel.SlowAnimations.getValue()) {
            info.setReturnValue(ItemViewModel.SlowVal.getValue().intValue());
        }
    }
}