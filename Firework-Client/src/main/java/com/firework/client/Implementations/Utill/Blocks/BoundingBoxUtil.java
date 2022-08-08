package com.firework.client.Implementations.Utill.Blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BoundingBoxUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static AxisAlignedBB getBB(BlockPos pos) {
        return new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX,
                                pos.getY() - mc.getRenderManager().viewerPosY,
                                pos.getZ() - mc.getRenderManager().viewerPosZ,

                pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                pos.getY() + 1 - mc.getRenderManager().viewerPosY,
                pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
    }

    public static AxisAlignedBB getBB(BlockPos pos, double height) {
        return new AxisAlignedBB(
                pos.getX() - mc.getRenderManager().viewerPosX,
                pos.getY() - mc.getRenderManager().viewerPosY,
                pos.getZ() - mc.getRenderManager().viewerPosZ,

                pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                pos.getY() + height - mc.getRenderManager().viewerPosY,
                pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
    }

    public static AxisAlignedBB getBBCSize(AxisAlignedBB axisAlignedBB, float progress) {
        double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
        double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
        double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
        double progressValX = progress * ((axisAlignedBB.maxX - centerX) / 10);
        double progressValY = progress * ((axisAlignedBB.maxY - centerY) / 10);
        double progressValZ = progress * ((axisAlignedBB.maxZ - centerZ) / 10);

        return new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
    }
}
