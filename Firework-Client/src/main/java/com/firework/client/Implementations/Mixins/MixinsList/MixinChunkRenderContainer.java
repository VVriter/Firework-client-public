package com.firework.client.Implementations.Mixins.MixinsList;
import com.firework.client.Implementations.Events.EventRenderChunkContainer;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderContainer.class)
public class MixinChunkRenderContainer {
    @Inject(method = "preRenderChunk", at = @At(value = "RETURN", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;getPosition()Lnet/minecraft/util/math/BlockPos/MutableBlockPos;"))
    private void preRenderChunk(RenderChunk renderChunk, CallbackInfo callbackInfo) {
        MinecraftForge.EVENT_BUS.post(new EventRenderChunkContainer(renderChunk));
    }
}