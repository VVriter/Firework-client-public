package com.firework.client.Implementations.Events.Chunk;

import net.minecraft.client.renderer.chunk.RenderChunk;
import ua.firework.beet.Event;

public class EventRenderChunkContainer extends Event {
    public net.minecraft.client.renderer.chunk.RenderChunk RenderChunk;
    public EventRenderChunkContainer(RenderChunk renderChunk)
    {
        RenderChunk = renderChunk;
    }
}
