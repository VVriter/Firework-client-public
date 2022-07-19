package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.EventRenderChunk;
import com.firework.client.Implementations.Events.EventRenderChunkContainer;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ModuleManifest(name = "ChunkAnimator",category = Module.Category.RENDER)
public class ChunkAnimator extends Module {

    public Setting<Double> AnimationLength = new Setting<>("AnimationLength", (double)250, this, 1, 5000);
  //  public Setting<Boolean> EasingEnabled = new Setting<>("EasingEnabled", false, this);

    private final WeakHashMap<RenderChunk, AtomicLong> lifespans = new WeakHashMap<>();
    private double easeOutCubic(double t)
    {
        return (--t) * t * t + 1;
    }


    @SubscribeEvent
    public void onRenderChunk(EventRenderChunk e) {
        if (Minecraft.getMinecraft().player != null) {
            if (!lifespans.containsKey(e.RenderChunk)) {
                lifespans.put(e.RenderChunk, new AtomicLong(-1L));
            }
        }
    }


    @SubscribeEvent
    public void chunkContainer(EventRenderChunkContainer e) {
        if (lifespans.containsKey(e.RenderChunk)) {
            AtomicLong timeAlive = lifespans.get(e.RenderChunk);
            long timeClone = timeAlive.get();
            if (timeClone == -1L) {
                timeClone = System.currentTimeMillis();
                timeAlive.set(timeClone);
            }

            long timeDifference = System.currentTimeMillis() - timeClone;
            if (timeDifference <= AnimationLength.getValue()) {
                double chunkY = e.RenderChunk.getPosition().getY();
                double offsetY = chunkY / AnimationLength.getValue() * timeDifference;
                GlStateManager.translate(0.0, -chunkY + offsetY, 0.0);
            }
        }
    }

}
