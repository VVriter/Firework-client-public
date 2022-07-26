package com.firework.client.Implementations.Managers;

import net.minecraft.client.Minecraft;
public class FpsManager {
    public static long currentFps;

    public static String getCurrendFps(){
        currentFps = Minecraft.getDebugFPS();
        return currentFps+"";
    }
}
