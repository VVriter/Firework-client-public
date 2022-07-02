package com.firework.client.Implementations.Mixins.MixinsList;

import com.firework.client.Firework;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Minecraft.class )
public class MixinMinecraft {
    @Inject( method = "getLimitFramerate", at = @At( "HEAD" ), cancellable = true )
    public void getLimitFramerate( CallbackInfoReturnable< Integer > info )
    {
        if( Firework.shaders.currentshader != null )
            info.setReturnValue( 60 );
    }
}