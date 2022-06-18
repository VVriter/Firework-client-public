package com.firework.client.Implementations.Utill;

import net.minecraft.client.Minecraft;

public class YawUtil {

    public static int ignoreTicks = 0;

    public static void MakeRoundedYaw(int speed, boolean diagonal){
        Minecraft mc = Minecraft.getMinecraft();
        float diff = 360 / (diagonal ? 8f : 4f);
        if (ignoreTicks <= 0) {
            float yaw = mc.player.rotationYaw + 180F;
            yaw = Math.round((yaw / diff)) * diff;
            yaw -= 180F;
            mc.player.prevRotationYaw = mc.player.rotationYaw;
            mc.player.rotationYaw = Interpolation.finterpTo(
                    mc.player.rotationYaw,
                    yaw,
                    mc.getRenderPartialTicks(),
                    speed
            );
        }
    }

}