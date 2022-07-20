package com.firework.client.Implementations.Utill.Blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BoundingBoxUtil {
    public static AxisAlignedBB getBB(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        return new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX,
                                pos.getY() - mc.getRenderManager().viewerPosY,
                                pos.getZ() - mc.getRenderManager().viewerPosZ,

                pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                pos.getY() + 1 - mc.getRenderManager().viewerPosY,
                pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
    }

    public static AxisAlignedBB getBB(BlockPos pos, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        return new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX,
                pos.getY() - mc.getRenderManager().viewerPosY,
                pos.getZ() - mc.getRenderManager().viewerPosZ,

                pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                pos.getY() + height - mc.getRenderManager().viewerPosY,
                pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
    }
}
