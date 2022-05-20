package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Objects;

public class MovementHelper extends Module {

    public static Setting<Boolean> enabled = null;
    public Setting<Boolean> antiLevitate = new Setting<>("AntiLevitate", true, this);
    public Setting<Boolean> sprint = new Setting<>("Sprint", true, this);
    public static Setting<Boolean> parkour = null;

    public MovementHelper(){super("MovementHelper",Category.MOVEMENT);
        parkour = new Setting<>("Parkour", true, this);
        enabled = this.isEnabled;}
    @Override
    public void tryToExecute(){
        super.tryToExecute();

        if(antiLevitate.getValue()){
            if (mc.player.isPotionActive((Potion) Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation")))) {
                mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
            }
        }
        if(parkour.getValue()){
            if(!AirJump.enabled.getValue()){
            if(sprint.getValue() && mc.player.moveForward > 0 && !mc.player.collidedHorizontally){
                mc.player.setSprinting(true);
            }
            if(!mc.player.onGround || mc.gameSettings.keyBindJump.isPressed()) return;
            if(mc.player.isSneaking() || mc.gameSettings.keyBindSneak.isPressed()) return;

            AxisAlignedBB entityBoundingBox = mc.player.getEntityBoundingBox();
            AxisAlignedBB offsetBox = entityBoundingBox.offset(0, -0.5, 0).expand(-0.001, 0, -0.001);

            List<AxisAlignedBB> collisionBoxes = mc.world.getCollisionBoxes(mc.player, offsetBox);

            if(!collisionBoxes.isEmpty()) return;

            mc.player.jump();}else {
                MessageUtil.sendError("U are dumb dont use air jump with parkour helper",-1117);
            }
        } if(sprint.getValue() && mc.player.moveForward > 0 && !mc.player.collidedHorizontally){
            mc.player.setSprinting(true);
        }
    }


    @SubscribeEvent
    public void Packet(PacketEvent event) {
        if (event.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
            if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
                event.setCanceled(true);
                }
            }
        }
    }
