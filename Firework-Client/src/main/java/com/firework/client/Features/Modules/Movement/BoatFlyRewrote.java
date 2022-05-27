package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.item.EntityBoat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

@ModuleArgs(name = "BoatFly",category = Module.Category.MOVEMENT)
public class BoatFlyRewrote extends Module {

    private EntityBoat target;
    private int teleportID;
    private int packetCounter = 0;

    public Setting<Boolean> fixYaw  = new Setting<>("FixYaw", true, this);

    public Setting<Boolean> noKick  = new Setting<>("AntiKick", true, this);


    public Setting<Double> speed  = new Setting<>("Speed", (double)3, this, 1, 10);
    public Setting<Double> verticalSpeed   = new Setting<>("VSpeed", (double)3, this, 1, 10);

    public Setting<Boolean> packet  = new Setting<>("packet", true, this);
    public Setting<Double> packets   = new Setting<>("packets", (double)3, this, 1, 10);

    public Setting<Double> interact    = new Setting<>("interact", (double)3, this, 1, 10);
    

    @Override
    public void onTick() {
        super.onTick();

        if (fixYaw.getValue()) {
            EntityBoat playerBoat = (EntityBoat) mc.player.getRidingEntity();
            playerBoat.rotationYaw = mc.player.rotationYaw;
        }
        
        
        if (mc.player == null) {
            return;
        }
        if (mc.world == null || mc.player.getRidingEntity() == null) {
            return;
        }
        if (mc.player.getRidingEntity() instanceof EntityBoat) {
            target = (EntityBoat)mc.player.getRidingEntity();
        }
        mc.player.getRidingEntity().setNoGravity(true);
        mc.player.getRidingEntity().motionY = 0.0;
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.getRidingEntity().onGround = false;
            mc.player.getRidingEntity().motionY = verticalSpeed.getValue() / 10.0;
        }
        if (mc.gameSettings.keyBindSprint.isKeyDown()) {
            mc.player.getRidingEntity().onGround = false;
            mc.player.getRidingEntity().motionY = -(verticalSpeed.getValue() / 10.0);
        }
        double[] normalDir = directionSpeed(speed.getValue() / 2.0);
        if (mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f) {
            mc.player.getRidingEntity().motionX = normalDir[0];
            mc.player.getRidingEntity().motionZ = normalDir[1];
        } else {
            mc.player.getRidingEntity().motionX = 0.0;
            mc.player.getRidingEntity().motionZ = 0.0;
        }
        if (noKick.getValue().booleanValue()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                if (mc.player.ticksExisted % 8 < 2) {
                    mc.player.getRidingEntity().motionY = -0.04f;
                }
            } else if (mc.player.ticksExisted % 8 < 4) {
                mc.player.getRidingEntity().motionY = -0.08f;
            }
        }
        handlePackets(mc.player.getRidingEntity().motionX, mc.player.getRidingEntity().motionY, mc.player.getRidingEntity().motionZ);
    }

    public void handlePackets(double x, double y, double z) {
        if (packet.getValue().booleanValue()) {
            Vec3d vec = new Vec3d(x, y, z);
            if (mc.player.getRidingEntity() == null) {
                return;
            }
            Vec3d position = mc.player.getRidingEntity().getPositionVector().add(vec);
            mc.player.getRidingEntity().setPosition(position.x, position.y, position.z);
            mc.player.connection.sendPacket((Packet)new CPacketVehicleMove(mc.player.getRidingEntity()));
            for (int i = 0; i < packets.getValue(); ++i) {
                mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(teleportID++));
            }
        }
    }

    private void NCPPacketTrick() {
        packetCounter = 0;
        mc.player.getRidingEntity().dismountRidingEntity();
        Entity l_Entity = mc.world.loadedEntityList.stream().filter(p_Entity -> p_Entity instanceof EntityBoat).min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity))).orElse(null);
        if (l_Entity != null) {
            mc.playerController.interactWithEntity(mc.player, l_Entity, EnumHand.MAIN_HAND);
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {

        if (mc.world != null && mc.player != null) {
            if (mc.player.getRidingEntity() instanceof EntityBoat) {
                if (noKick.getValue() && event.getPacket() instanceof CPacketInput && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.player.getRidingEntity().onGround) {
                    ++packetCounter;
                    if (packetCounter == 3) {
                        NCPPacketTrick();
                    }
                }
                if (noKick.getValue() && event.getPacket() instanceof SPacketPlayerPosLook || event.getPacket() instanceof SPacketMoveVehicle) {
                    event.setCanceled(true);
                }
            }
        }

        if (event.getPacket() instanceof CPacketVehicleMove && mc.player.isRiding() && mc.player.ticksExisted % interact.getValue() == 0) {
            mc.playerController.interactWithEntity((EntityPlayer)mc.player, mc.player.getRidingEntity(), EnumHand.OFF_HAND);
        }
        if ((event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketInput) && mc.player.isRiding()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketMoveVehicle && mc.player.isRiding()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            teleportID = ((SPacketPlayerPosLook)event.getPacket()).getTeleportId();
        }
    }

    private double[] directionSpeed(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.player.getRidingEntity().setNoGravity(false);
    }
    
}
