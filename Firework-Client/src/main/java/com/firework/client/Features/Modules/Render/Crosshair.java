package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@ModuleManifest(name = "Crosshair",category = Module.Category.RENDER)
public class Crosshair extends Module {

    @SubscribeEvent
    public void render(TickEvent.RenderTickEvent e) {
        
    }

    @SubscribeEvent
    public void onRenderCrosshair(RenderGameOverlayEvent.Pre e) {
        if(e.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
           e.setCanceled(true);
        }
    }
}
