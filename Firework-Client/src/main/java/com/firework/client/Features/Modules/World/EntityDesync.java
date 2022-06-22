package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@ModuleManifest(name = "EntityDesync",category = Module.Category.WORLD)
public class EntityDesync extends Module {
    private static Minecraft mc = Minecraft.getMinecraft();
    private Entity Riding;
    @Override
    public void onEnable() {
        super.onEnable();
        if (EntityDesync.mc.player == null) {
            this.Riding = null;
            this.isEnabled.setValue(false);
            return;
        }
        if (!EntityDesync.mc.player.isRiding()) {
            MessageUtil.sendClientMessage("You are not riding an entity",-1117);
            this.Riding = null;
            this.isEnabled.setValue(false);
            return;
        }
        this.Riding = EntityDesync.mc.player.getRidingEntity();
        EntityDesync.mc.player.dismountRidingEntity();
        EntityDesync.mc.world.removeEntity(this.Riding);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.Riding != null) {
            this.Riding.isDead = false;
            if (!EntityDesync.mc.player.isRiding()) {
                EntityDesync.mc.world.spawnEntity(this.Riding);
                EntityDesync.mc.player.startRiding(this.Riding, true);
            }
            this.Riding = null;
            MessageUtil.sendClientMessage("Forced a remount",-1117);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.Riding == null) {
            return;
        }
        if (EntityDesync.mc.player.isRiding()) {
            return;
        }
        EntityDesync.mc.player.onGround = true;
        this.Riding.setPosition(EntityDesync.mc.player.posX, EntityDesync.mc.player.posY, EntityDesync.mc.player.posZ);
        EntityDesync.mc.player.connection.sendPacket((Packet)new CPacketVehicleMove(this.Riding));
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSetPassengers) {
            if (this.Riding == null) {
                return;
            }
            final SPacketSetPassengers l_Packet = (SPacketSetPassengers) event.getPacket();
            final Entity en = EntityDesync.mc.world.getEntityByID(l_Packet.getEntityId());
            if (en == this.Riding) {
                for (final int i : l_Packet.getPassengerIds()) {
                    final Entity ent = EntityDesync.mc.world.getEntityByID(i);
                    if (ent == EntityDesync.mc.player) {
                        return;
                    }
                }
                MessageUtil.sendClientMessage("You dismounted",-1117);
                this.isEnabled.setValue(false);
            }
        }
        else if (event.getPacket() instanceof SPacketDestroyEntities) {
            final SPacketDestroyEntities l_Packet2 = (SPacketDestroyEntities) event.getPacket();
            for (final int l_EntityId : l_Packet2.getEntityIDs()) {
                if (l_EntityId == this.Riding.getEntityId()) {
                    MessageUtil.sendClientMessage("Entity is now null SPacketDestroyEntities",-1117);
                    return;
                }
            }
        }
    }
}
