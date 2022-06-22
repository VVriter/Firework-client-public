package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

import java.util.Arrays;

@ModuleManifest(name = "Fly",category = Module.Category.MOVEMENT)
public class Fly extends Module {
    public Setting<String> mode  = new Setting<>("Mode", "Vanilla", this, Arrays.asList("Vanilla", "Motion", "Tp", "Servers"));

    public Setting<Double> speed  = new Setting<>("Speed ", (double)3, this, 1, 10);

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mode.getValue().equals("Clicktp")) {
            if (mc.gameSettings.keyBindAttack.isPressed()) {
                mc.player.capabilities.isFlying = true;
                double yaw = mc.player.rotationYaw;
                float increment = +8.5F;
                mc.player.setPosition(mc.player.posX + Math.sin(Math.toRadians(-yaw)) * increment, mc.player.posY, mc.player.posZ + Math.cos(Math.toRadians(-yaw)) * increment);
            }
        }




        if (mode.getValue().equals("Tp")) {

            EntityPlayerSP player = mc.player;
            player.capabilities.isFlying = false;
            final float speedScaled = (float) (this.speed.getValue() * 0.5f);

            final double[] directionSpeedVanilla = directionSpeed(speedScaled);
            if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
                player.setPosition(player.posX + directionSpeedVanilla[0], player.posY, player.posZ + directionSpeedVanilla[1]);

            player.motionX = 0;
            player.motionZ = 0;
            player.motionY = 0;
            player.setVelocity(0, 0, 0);
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                player.setPosition(player.posX,
                        player.posY + speed.getValue(), player.posZ);
                player.motionY = 0;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown() && !player.onGround) {
                player.setPosition(player.posX,
                        player.posY - speed.getValue(), player.posZ);
            }

        }

        if (mode.getValue().equals("Motion")) {

            final float speedScaled = (float) (this.speed.getValue() * 0.6f);

            final double[] directionSpeedVanilla = directionSpeed(speedScaled);
            if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0) {
                mc.player.motionX = directionSpeedVanilla[0];
                mc.player.motionZ = directionSpeedVanilla[1];
            }
            mc.player.motionY = 0;
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = speed.getValue();
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.player.onGround) {
                mc.player.motionY = -speed.getValue();
            }

        }
        if (mode.getValue().equals("Vanilla")) {
            EntityPlayerSP player = mc.player;

            player.capabilities.isFlying = false;
            player.motionX = 0;
            player.motionY = 0;
            player.motionZ = 0;
            player.jumpMovementFactor = (float) speed.getValue().floatValue();

            if (mc.gameSettings.keyBindJump.isKeyDown())
                player.motionY += speed.getValue();
            if (mc.gameSettings.keyBindSneak.isKeyDown())
                player.motionY -= speed.getValue();
        }
        if (mode.getValue().equals("Servers")) {
            double y1;
            mc.player.motionY = 0;
            if (mc.player.ticksExisted % 3 == 0) {
                double y = mc.player.posY - 1.0E-10D;
            }
            y1 = mc.player.posY + 1.0E-10D;
            mc.player.setPosition(mc.player.posX, y1, mc.player.posZ);
        }


    }

    public void utils(float speed) {
        mc.player.motionX = (-(Math.sin(aan()) * speed));
        mc.player.motionZ = (Math.cos(aan()) * speed);
    }

    public float aan() {
        float var1 = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0F) {
            var1 += 180.0F;
        }
        float forward = 1.0F;
        if (mc.player.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (mc.player.moveForward > 0.0F) {
            forward = 0.5F;
        }
        if (mc.player.moveStrafing > 0.0F) {
            var1 -= 90.0F * forward;
        }
        if (mc.player.moveStrafing < 0.0F) {
            var1 += 90.0F * forward;
        }
        var1 *= 0.017453292F;

        return var1;
    }

    private void Movement(float forward, float strafe, EntityPlayerSP player, float var5, float var6) {
        double speed;
        mc.player.motionX = player.posX * 5.01E-8;
        speed = 2.7999100260353087;
        mc.player.motionZ = player.posZ * 5.01E-8;
        mc.player.motionZ = player.posZ * 5.01E-8;

        Runmove(player);
        Runmove(player);
        mc.player.motionX = player.posX * 5.01E-8;
        double motionX = (double) (strafe * var6 - forward * var5) * speed;
        double motionZ = (double) (forward * var6 + strafe * var5) * speed;
        mc.player.motionX = motionX;
        mc.player.motionZ = motionZ;
        if (!mc.gameSettings.keyBindJump.isPressed()) {
            mc.player.setPosition(mc.player.posX, mc.player.posY - 0.03948584, mc.player.posZ);
        }
    }

    private void Runmove(EntityPlayerSP player) {
        mc.player.motionZ = player.posZ * 5.01E-8;
        mc.player.motionX = player.posX * 5.01E-8;
        mc.player.motionZ = player.posZ * 5.01E-8;
        mc.player.motionX = player.posX * 5.01E-8;
        mc.player.motionZ = player.posZ * 5.01E-8;
        mc.player.motionX = player.posX * 5.01E-8;
        mc.player.motionZ = player.posZ * 5.01E-8;
        mc.player.motionX = player.posX * 5.01E-8;
        mc.player.motionZ = player.posZ * 5.01E-8;
        mc.player.motionX = player.posX * 5.01E-8;
    }

    private void SendPacket() {
        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? 0.0621 : 0.0) - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.0621 : 0.0), mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY - 999.0, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
    }

    public double getPosForSetPosX(double value) {
        double yaw = Math.toRadians(mc.player.rotationYaw);
        double x = -Math.sin(yaw) * value;
        return x;
    }

    public double getPosForSetPosZ(double value) {
        double yaw = Math.toRadians(mc.player.rotationYaw);
        double z = Math.cos(yaw) * value;
        return z;
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        super.onDisable();
    }

    public static double[] directionSpeed(double speed) {
        float forward = Wrapper.INSTANCE.mc().player.movementInput.moveForward;
        float side = Wrapper.INSTANCE.mc().player.movementInput.moveStrafe;
        float yaw = Wrapper.INSTANCE.mc().player.prevRotationYaw + (Wrapper.INSTANCE.mc().player.rotationYaw - Wrapper.INSTANCE.mc().player.prevRotationYaw) * Wrapper.INSTANCE.mc().getRenderPartialTicks();

        if (forward != 0) {
            if (side > 0)
                yaw += (forward > 0 ? -45 : 45);
            else if (side < 0)
                yaw += (forward > 0 ? 45 : -45);
            side = 0;
            if (forward > 0)
                forward = 1;
            else if (forward < 0)
                forward = -1;
        }

        final double sin = sin(Math.toRadians(yaw + 90));
        final double cos = cos(Math.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]{posX, posZ};
    }

}
