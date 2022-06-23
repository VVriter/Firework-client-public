package com.firework.client.Implementations.Hud.Huds.Render.MemoryHud;

public class MemoryManager {
    public static long memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;
    public static long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
    public static long allocatedMemory = Runtime.getRuntime().totalMemory() / 1048576L;
}
