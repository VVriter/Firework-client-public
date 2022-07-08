package com.firework.client.Implementations.Events;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventRenderChunk extends Event {
    public net.minecraft.util.math.BlockPos BlockPos;
    public net.minecraft.client.renderer.chunk.RenderChunk RenderChunk;
    public EventRenderChunk(RenderChunk renderChunk, BlockPos blockPos)
    {
        BlockPos = blockPos;
        RenderChunk = renderChunk;
    }
}
