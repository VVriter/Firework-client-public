package com.firework.client.Implementations.Utill.Blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class PredictPlace {

    private Minecraft mc = Minecraft.getMinecraft();

    private final EnumFacing facing;
    private final Vec3d hitVec;

    public PredictPlace(EnumFacing facing, Vec3d hitVec){
        this.facing = facing;
        this.hitVec = hitVec;
    }

    public EnumFacing getFacing(){
        return this.facing;
    }

    public Vec3d getHitVec(){
        return this.hitVec;
    }

    public double getDistance(){
        final Vec3d eyesPos = mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0);
        return hitVec.distanceTo(eyesPos);
    }

    public double getNegativeDistance(){
        final Vec3d eyesPos = mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0);
        return -hitVec.distanceTo(eyesPos);
    }

}
