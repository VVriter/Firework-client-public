package com.firework.client.Implementations.Hud.Huds.Render.FpsHud;

import net.minecraft.client.Minecraft;
public class FpsManager {
    public static long currentFps;

    public static String getCurrendFps(){
        currentFps = Minecraft.getDebugFPS();
        return currentFps+"";
    }
}
