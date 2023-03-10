package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayerTryUseItemOnBlock;
import com.firework.client.Implementations.Mixins.MixinsList.IEntity;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.item.ItemBoat;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Bypass",
        category = Module.Category.WORLD,
        description = "Some bypasses"
)
public class Bypass extends Module {

    public Setting<Boolean> boatPlace = new Setting<>("BoatPlace", true, this);
    public Setting<Boolean> buildHeight  = new Setting<>("BuildHeight", true, this);
    public Setting<Boolean> portalChat  = new Setting<>("PortalGui", true, this);

    public Setting<Boolean> mountBypass  = new Setting<>("MountBypass", true, this);

    public Setting<Boolean> worldBorder  = new Setting<>("WorldBorder", true, this);

    @Override
    public void onTick(){

        if (portalChat.getValue()) {
            ((IEntity) mc.player).setInPortal(false);
        }


       if(boatPlace.getValue()){
           if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBoat && Mouse.isButtonDown(1)) {
               mc.getConnection().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
               mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(mc.objectMouseOver.getBlockPos(), EnumFacing.SOUTH, EnumHand.MAIN_HAND, 1, 1, 1));
           }
       }
    }

    @Subscribe
    public Listener<PacketEvent> onRender = new Listener<>(event -> {
        if(buildHeight.getValue()){
        if (mc.player == null) return;
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                final CPacketPlayerTryUseItemOnBlock oldPacket = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
                if (oldPacket.getPos().getY() >= 255) {
                    if (oldPacket.getDirection() == EnumFacing.UP) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(oldPacket.getPos(), EnumFacing.DOWN, oldPacket.getHand(), oldPacket.getFacingX(), oldPacket.getFacingY(), oldPacket.getFacingZ()));
                        event.setCancelled(true);
                        }
                    }
                    }
                }
            });

            @Subscribe
            public Listener<PacketEvent.Send> onRender1 = new Listener<>(event -> {

                if (worldBorder.getValue()) {
                    if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                        CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
                        if (packet.getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
                            ((ICPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.DOWN);
                        } else if (!mc.world.getWorldBorder().contains(packet.getPos())) {
                            switch (packet.getDirection()) {
                                case EAST:
                                    ((ICPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.WEST);
                                    break;
                                case NORTH:
                                    ((ICPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.SOUTH);
                                    break;
                                case WEST:
                                    ((ICPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.EAST);
                                    break;
                                case SOUTH:
                                    ((ICPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.NORTH);
                                    break;
                            }
                        }
                    }
                }

                if (event.getPacket() instanceof CPacketUseEntity)
                {
                    CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();

                    if (packet.getEntityFromWorld(mc.world) instanceof AbstractChestHorse)
                    {
                        if (packet.getAction() == CPacketUseEntity.Action.INTERACT_AT)
                        {
                            event.setCancelled(true);
                        }
                    }
                }
            });


        }

