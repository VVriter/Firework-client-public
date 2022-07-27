package com.firework.client.Implementations.Mixins.MixinsList.Chunks;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Chunk.EventRenderChunkContainer;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderContainer.class)
public class MixinChunkRenderContainer {
    @Inject(method = "preRenderChunk", at = @At(value = "RETURN", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;getPosition()Lnet/minecraft/util/math/BlockPos/MutableBlockPos;"))
    private void preRenderChunk(RenderChunk renderChunk, CallbackInfo callbackInfo) {
       Firework.eventBus.post(new EventRenderChunkContainer(renderChunk));
    }
}