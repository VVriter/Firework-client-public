package com.firework.client.Implementations.Events;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventRenderChunkContainer extends Event {
    public net.minecraft.client.renderer.chunk.RenderChunk RenderChunk;
    public EventRenderChunkContainer(RenderChunk renderChunk)
    {
        RenderChunk = renderChunk;
    }
}
