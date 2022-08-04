package com.firework.client.Implementations.Managers;

import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

public class RotationManager extends Manager{

    public RotationManager() {
        super(true);
    }

    public boolean isRotateSpoofing;
    public float yaw, pitch;
    private int yawPlusCount, yawMinusCount;

    private Minecraft mc = Minecraft.getMinecraft();

    public void rotate(Vec3d vec, boolean sendPacket) {
        float[] rotations = getRotations(vec);

        if (sendPacket) mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
        mc.player.rotationYaw = rotations[0];
        mc.player.rotationPitch = rotations[1];
    }

    public void setPlayerRotations(float yaw, float pitch) {
       mc.player.rotationYaw = yaw;
       mc.player.rotationYawHead = yaw;
       mc.player.rotationPitch = pitch;
    }
    public void rotateSpoof(Vec3d vec) {
        float[] rotations = getRotations(vec);
        yaw = rotations[0];
        pitch = rotations[1];

        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
        isRotateSpoofing = true;
    }

    public void rotateSpoofNoEvent(Vec3d vec) {
        float[] rotations = getRotations(vec);
        yaw = rotations[0];
        pitch = rotations[1];

        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
        isRotateSpoofing = true;
    }

    public void rotateSpoofNoPacket(Vec3d vec) {
        float[] rotations = getRotations(vec);
        yaw = rotations[0];
        pitch = rotations[1];
        isRotateSpoofing = true;
    }

    public void stopRotating() {
        isRotateSpoofing = false;
    }

    public float[] getRotations(Vec3d vec) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    @Subscribe
    public Listener<PacketEvent.Receive> onRender = new Listener<>(event -> {
        if (isRotateSpoofing) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                CPacketPlayer.Rotation packet = (CPacketPlayer.Rotation) event.getPacket();
                ((ICPacketPlayer) packet).setYaw(yaw);
                ((ICPacketPlayer) packet).setPitch(pitch);
            } else if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                CPacketPlayer.PositionRotation packet = (CPacketPlayer.PositionRotation) event.getPacket();
                ((ICPacketPlayer) packet).setYaw(yaw);
                ((ICPacketPlayer) packet).setPitch(pitch);
            }
        }
    });

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if(mc.player == null || mc.world == null) return;
        try {
            if (yawMinusCount < 10) {
                yawMinusCount++;
                mc.player.rotationYaw -= 0.005;
            } else if (yawPlusCount < 10) {
                yawPlusCount++;
                mc.player.rotationYaw += 0.005;
            } else {
                yawMinusCount = 0;
                yawPlusCount = 0;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
