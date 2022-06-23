package com.firework.client.Implementations.Hud.Huds.Render.FpsHud;

import com.firework.client.Features.Modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FpsManager {
    public static long currentFps;

    public static String getCurrendFps(){
        currentFps = Minecraft.getDebugFPS();
        return currentFps+"";
    }
}
