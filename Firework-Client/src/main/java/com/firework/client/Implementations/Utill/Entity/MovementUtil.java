package com.firework.client.Implementations.Utill.Entity;

import com.firework.client.Implementations.Utill.Client.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.Vector2f;

public class MovementUtil{
    static Minecraft mc = Minecraft.getMinecraft();
    public static final float PI2 = Math.round((Math.PI * 2D));

    public static float getMoveYaw(float yaw) {
        Vector2f from = new Vector2f((float) mc.player.lastTickPosX, (float) mc.player.lastTickPosZ),
                to = new Vector2f((float) mc.player.posX, (float) mc.player.posZ),
                diff = new Vector2f(to.x - from.x, to.y - from.y);

        double x = diff.x, z = diff.y;
        if (x != 0 || z != 0) {
            yaw = (float) Math.toDegrees((Math.atan2(-x, z) + PI2) % PI2);
        }
        return yaw;
    }
    public static Vec3d calculateLine(Vec3d x1, Vec3d x2, double distance) {
        double length = Math.sqrt(MovementUtil.multiply(x2.x - x1.x) + MovementUtil.multiply(x2.y - x1.y) + MovementUtil.multiply(x2.z - x1.z));
        double unitSlopeX = (x2.x - x1.x) / length;
        double unitSlopeY = (x2.y - x1.y) / length;
        double unitSlopeZ = (x2.z - x1.z) / length;
        double x = x1.x + unitSlopeX * distance;
        double y = x1.y + unitSlopeY * distance;
        double z = x1.z + unitSlopeZ * distance;
        return new Vec3d(x, y, z);
    }

    public static Vec2f calculateLineNoY(Vec2f x1, Vec2f x2, double distance) {
        double length = Math.sqrt(MovementUtil.multiply(x2.x - x1.x) + MovementUtil.multiply(x2.y - x1.y));
        double unitSlopeX = (double)(x2.x - x1.x) / length;
        double unitSlopeZ = (double)(x2.y - x1.y) / length;
        float x = (float)((double)x1.x + unitSlopeX * distance);
        float z = (float)((double)x1.y + unitSlopeZ * distance);
        return new Vec2f(x, z);
    }

    public static double multiply(double one) {
        return one * one;
    }

    public static Vec3d extrapolatePlayerPositionWithGravity(EntityPlayer player, int ticks) {
        double totalDistance = 0.0;
        double extrapolatedMotionY = player.motionY;
        for (int i = 0; i < ticks; ++i) {
            totalDistance += MovementUtil.multiply(player.motionX) + MovementUtil.multiply(extrapolatedMotionY) + MovementUtil.multiply(player.motionZ);
            extrapolatedMotionY -= 0.1;
        }
        double horizontalDistance = MovementUtil.multiply(player.motionX) + MovementUtil.multiply(player.motionZ) * (double)ticks;
        Vec2f horizontalVec = MovementUtil.calculateLineNoY(new Vec2f((float)player.lastTickPosX, (float)player.lastTickPosZ), new Vec2f((float)player.posX, (float)player.posZ), horizontalDistance);
        double addedY = player.motionY;
        double finalY = player.posY;
        Vec3d tempPos = new Vec3d((double)horizontalVec.x, player.posY, (double)horizontalVec.y);
        for (int i = 0; i < ticks; ++i) {
            finalY += addedY;
            addedY -= 0.1;
        }
        RayTraceResult result = mc.world.rayTraceBlocks(player.getPositionVector(), new Vec3d(tempPos.x, finalY, tempPos.z));
        if (result == null || result.typeOfHit == RayTraceResult.Type.ENTITY) {
            return new Vec3d(tempPos.x, finalY, tempPos.z);
        }
        return result.hitVec;
    }

    public static Vec3d extrapolatePlayerPosition(EntityPlayer player, int ticks) {
        double totalDistance = 0.0;
        double extrapolatedMotionY = player.motionY;
        for (int i = 0; i < ticks; ++i) {
        }
        Vec3d lastPos = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
        Vec3d currentPos = new Vec3d(player.posX, player.posY, player.posZ);
        double distance = MovementUtil.multiply(player.motionX) + MovementUtil.multiply(player.motionY) + MovementUtil.multiply(player.motionZ);
        double extrapolatedPosY = player.posY;
        if (!player.hasNoGravity()) {
            extrapolatedPosY -= 0.1;
        }
        Vec3d tempVec = MovementUtil.calculateLine(lastPos, currentPos, distance * (double)ticks);
        Vec3d finalVec = new Vec3d(tempVec.x, extrapolatedPosY, tempVec.z);
        RayTraceResult result = MovementUtil.mc.world.rayTraceBlocks(player.getPositionVector(), finalVec);
        return new Vec3d(tempVec.x, player.posY, tempVec.z);
    }
}