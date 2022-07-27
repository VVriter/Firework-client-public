package com.firework.client.Implementations.Events.Chunk;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Event;

public class EventRenderChunk extends Event {
    public net.minecraft.util.math.BlockPos BlockPos;
    public net.minecraft.client.renderer.chunk.RenderChunk RenderChunk;
    public EventRenderChunk(RenderChunk renderChunk, BlockPos blockPos)
    {
        BlockPos = blockPos;
        RenderChunk = renderChunk;
    }
}
