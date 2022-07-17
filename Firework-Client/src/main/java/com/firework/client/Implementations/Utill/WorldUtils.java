package com.firework.client.Implementations.Utill;

public class WorldUtils {
    public static void reloadChunks() {
        if (Globals.MC.world != null && Globals.MC.player != null) {
            Globals.MC.addScheduledTask(() -> {
                int x = (int)Globals.MC.player.posX;
                int y = (int)Globals.MC.player.posY;
                int z = (int)Globals.MC.player.posZ;
                int distance = Globals.MC.gameSettings.renderDistanceChunks * 16;
                Globals.MC.renderGlobal.markBlockRangeForRenderUpdate(x - distance, y - distance, z - distance, x + distance, y + distance, z + distance);
            });
        }
    }
}
