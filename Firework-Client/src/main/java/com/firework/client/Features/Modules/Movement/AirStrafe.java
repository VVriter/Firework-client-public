package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ModuleManifest(name = "AirStrafe",category = Module.Category.MOVEMENT)
public class AirStrafe extends Module {
    private double currentSpeed = 0.0D;
    private double prevMotion = 0.0D;
    private boolean oddStage = false;
    private int state = 4;

    @Override
    public void onTick() {
        super.onTick();
        if (mc.player.onGround || mc.player.isElytraFlying() || mc.player.capabilities.isFlying) return;
        if (state != 1 || (mc.player.moveForward == 0.0f || mc.player.moveStrafing == 0.0f)) {
            if (state == 2 && (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f)) {
                currentSpeed *= oddStage ? 1.6835D : 1.395D;
            } else if (state == 3) {
                double adjustedMotion = 0.66D * (prevMotion - getBaseMotionSpeed());
                currentSpeed = prevMotion - adjustedMotion;
                oddStage = !oddStage;
            } else {
                List<AxisAlignedBB> collisionBoxes = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0));
                if ((collisionBoxes.size() > 0 || mc.player.collidedVertically) && state > 0) {
                    state = mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f ? 0 : 1;
                }
                currentSpeed = prevMotion - prevMotion / 159.0;
            }
        } else {
            currentSpeed = 1.35D * getBaseMotionSpeed() - 0.01D;
        }

        currentSpeed = Math.max(currentSpeed, getBaseMotionSpeed());

        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;

        if (forward == 0.0D && strafe == 0.0D) {
            mc.player.motionX = 0.0D;
            mc.player.motionZ = 0.0D;
        } else {
            if (forward != 0.0D) {

                if (strafe > 0.0D) {
                    yaw += (float)(forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (float)(forward > 0.0D ? 45 : -45);
                }

                strafe = 0.0D;

                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }

            mc.player.motionX = (forward * currentSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * currentSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
            mc.player.motionZ =(forward * currentSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * currentSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }


        if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f) {
            return;
        }

        state++;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            currentSpeed = 0.0D;
            state = 4;
            prevMotion = 0;
        }
    }

    private double getBaseMotionSpeed() {
        double baseSpeed = 0.2873D;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * ((double) amplifier + 1);
        }
        return baseSpeed;
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        state = 4;
        currentSpeed = getBaseMotionSpeed();
        prevMotion = 0;

    }

  /*  @Subscriber
    public void onPlayerWalkingUpdate(UpdateWalkingPlayerEvent.Pre event) {
        if (!PlayerUtils.isPlayerMoving()) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
            currentSpeed = 0.0;
            return;
        }
        double dX = mc.player.posX - mc.player.prevPosX;
        double dZ = mc.player.posZ - mc.player.prevPosZ;
        prevMotion = Math.sqrt(dX * dX + dZ * dZ);
    }*/
}

