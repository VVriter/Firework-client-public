package com.firework.client.Implementations.Managers.Coords;

import net.minecraft.client.Minecraft;

public class CoordsManager {
   static Minecraft mc = Minecraft.getMinecraft();

    public static String getCordsForDrawString(){
        if(mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell")){
            int posX = (int) mc.player.posX*8;
            int posY = (int) mc.player.posY;
            int posZ = (int) mc.player.posZ*8;

            return "X: "+posX+" Y: "+posY+" Z: "+posZ;
        }else{
            int posX = (int) mc.player.posX;
            int posY = (int) mc.player.posY;
            int posZ = (int) mc.player.posZ;
            return "X: "+posX+" Y: "+posY+" Z: "+posZ;
        }
    }
}
