package com.firework.client.Implementations.Utill.Blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PredictPlace {

    private Minecraft mc = Minecraft.getMinecraft();

    private final EnumFacing facing;
    private final Vec3d hitVec;

    public PredictPlace(EnumFacing facing, BlockPos pos){
        this.facing = facing;
        this.hitVec = BlockUtil.offset(new Vec3d(pos).add(0.5, -0.5, 0.5), facing,0.5f);
    }

    public EnumFacing getFacing(){
        return this.facing;
    }

    public Vec3d getHitVec(){
        return this.hitVec;
    }

    public int getOffsetFactor(){
        return this.facing.getXOffset() + this.facing.getYOffset() + this.facing.getZOffset();
    }

    public Vec3d getTranslatedEyes(){
        final Vec3d eyesPos = mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0);
        return getTranslated(eyesPos);
    }

    public Vec3d getTranslated(Vec3d vec){
        return new Vec3d(vec.x * this.facing.getXOffset(),
                vec.y * this.facing.getYOffset(),
                vec.z * this.facing.getZOffset());
    }

    public double eyesOffset(){
        return offset(getTranslatedEyes());
    }

    public double offset(Vec3d vec){
        return vec.x + vec.y + vec.z;
    }

}
