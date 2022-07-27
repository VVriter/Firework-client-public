package com.firework.client.Implementations.Mixins.MixinsList.Chunks;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Chunk.EventRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderChunk.class)
public class MixinRenderChunk {
    @Inject(method = "setPosition", at = @At("INVOKE"))
    private void setPosition(int x, int y, int z, CallbackInfo callbackInfo) {
        Firework.eventBus.post(new EventRenderChunk((RenderChunk) (Object) this, new BlockPos(x, y, z)));
    }
}