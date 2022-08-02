package com.firework.client.Implementations.Utill;

import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + (double) RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - RotationUtil.mc.player.rotationYaw), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - RotationUtil.mc.player.rotationPitch)};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = RotationUtil.getLegitRotations(vec);
        RotationUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float) MathHelper.normalizeAngle((int) rotations[1], 360) : rotations[1], RotationUtil.mc.player.onGround));
    }

    public static void rotate(Vec3d vec, boolean sendPacket) {
        float[] rotations = getRotations(vec);

        if (sendPacket) mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
        mc.player.rotationYaw = rotations[0];
        mc.player.rotationPitch = rotations[1];
    }

    public static float[] getRotations(Vec3d vec) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[] { mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
    }

    public static void packetFacePitchAndYaw(float p_Pitch, float p_Yaw)
    {
        boolean l_IsSprinting = mc.player.isSprinting();

        if (l_IsSprinting != ((IEntityPlayerSP)mc.player).getServerSprintState()) {
            if (l_IsSprinting) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }

            ((IEntityPlayerSP)mc.player).setServerSprintState(l_IsSprinting);
        }

        boolean l_IsSneaking = mc.player.isSneaking();

        if (l_IsSneaking != ((IEntityPlayerSP)mc.player).getServerSneakState()) {
            if (l_IsSneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }

            ((IEntityPlayerSP)mc.player).setServerSneakState(l_IsSneaking);
        }

        if (mc.getRenderViewEntity() == mc.player) {
            float l_Pitch = p_Pitch;
            float l_Yaw = p_Yaw;

            AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
            double l_PosXDifference = mc.player.posX - ((IEntityPlayerSP)mc.player).getLastReportedPosX();
            double l_PosYDifference = axisalignedbb.minY - ((IEntityPlayerSP)mc.player).getLastReportedPosY();
            double l_PosZDifference = mc.player.posZ - ((IEntityPlayerSP)mc.player).getLastReportedPosZ();
            double l_YawDifference = l_Yaw - ((IEntityPlayerSP)mc.player).getLastReportedYaw();
            double l_RotationDifference = l_Pitch - ((IEntityPlayerSP)mc.player).getLastReportedPitch();
            ((IEntityPlayerSP)mc.player).setPositionUpdateTicks(((IEntityPlayerSP)mc.player).getPositionUpdateTicks()+1);
            boolean l_MovedXYZ = l_PosXDifference * l_PosXDifference + l_PosYDifference * l_PosYDifference + l_PosZDifference * l_PosZDifference > 9.0E-4D || ((IEntityPlayerSP)mc.player).getPositionUpdateTicks() >= 20;
            boolean l_MovedRotation = l_YawDifference != 0.0D || l_RotationDifference != 0.0D;

            if (mc.player.isRiding()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, l_Yaw, l_Pitch, mc.player.onGround));
                l_MovedXYZ = false;
            }
            else if (l_MovedXYZ && l_MovedRotation) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, axisalignedbb.minY, mc.player.posZ, l_Yaw, l_Pitch, mc.player.onGround));
            }
            else if (l_MovedXYZ) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY, mc.player.posZ, mc.player.onGround));
            }
            else if (l_MovedRotation) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(l_Yaw, l_Pitch, mc.player.onGround));
            }
            else if (((IEntityPlayerSP)mc.player).getPrevOnGround() != mc.player.onGround) {
                mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
            }

            if (l_MovedXYZ) {
                ((IEntityPlayerSP)mc.player).setLastReportedPosX(mc.player.posX);
                ((IEntityPlayerSP)mc.player).setLastReportedPosY(axisalignedbb.minY);
                ((IEntityPlayerSP)mc.player).setLastReportedPosZ(mc.player.posZ);
                ((IEntityPlayerSP)mc.player).setPositionUpdateTicks(0);
            }

            if (l_MovedRotation) {
                ((IEntityPlayerSP)mc.player).setLastReportedYaw(l_Yaw);
                ((IEntityPlayerSP)mc.player).setLastReportedPitch(l_Pitch);
            }

            ((IEntityPlayerSP)mc.player).setPrevOnGround(mc.player.onGround);
            ((IEntityPlayerSP)mc.player).setAutoJumpEnabled(mc.gameSettings.autoJump);
        }
    }

}
