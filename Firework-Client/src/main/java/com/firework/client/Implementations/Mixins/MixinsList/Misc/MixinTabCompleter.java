package com.firework.client.Implementations.Mixins.MixinsList.Misc;

import net.minecraft.client.Minecraft;
import net.minecraft.util.TabCompleter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TabCompleter.class)
public abstract class MixinTabCompleter
{
    @Inject(method = "requestCompletions", at = @At("HEAD"), cancellable = true)
    private void requestCompletionsHook(String prefix, CallbackInfo ci)
    {
        if (Minecraft.getMinecraft().player == null)
        {
            ci.cancel();
        }
    }

}