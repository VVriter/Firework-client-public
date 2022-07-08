package com.firework.client.Implementations.Utill.Entity;

import com.firework.client.Features.Modules.Movement.BlockFlyRewrite;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.client.CPacketPlayer;

import static com.firework.client.Implementations.Utill.Util.mc;

public class MotionUtil {
    //Centers player given mode
    public static void autoCenter(Setting setting){
        if (setting.getValue(centerModes.Motion)) {
                MotionUtil.centerMotion();
        } else if (setting.getValue(centerModes.TP)) {
            MotionUtil.centerTeleport();
        }
    }

    //Moves player to a center
    public static void centerMotion() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};

        mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
        mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;
    }

    //Teleports player to a center
    public static void centerTeleport() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        mc.player.connection.sendPacket(new CPacketPlayer.Position(centerPos[0], mc.player.posY, centerPos[2], mc.player.onGround));
        mc.player.setPosition(centerPos[0], mc.player.posY, centerPos[2]);
    }

    //Checks if player was centered
    public static boolean isCentered() {
        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        return Math.abs(centerPos[0] - mc.player.posX) <= 0.1 && Math.abs(centerPos[2] - mc.player.posZ) <= 0.1;
    }

    //Center modes
    public enum centerModes{
        Motion, TP, NONE
    }
}
