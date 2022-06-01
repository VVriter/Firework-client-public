package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Events.ItemUsedEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@ModuleArgs(name = "ChorusControl",category = Module.Category.WORLD)
public class ChorusControl extends Module {
    private SPacketPlayerPosLook packet;
    private boolean consumed = false;



    @Override
    public void onTick() {
        super.onTick();
        if (consumed && mc.gameSettings.keyBindSneak.isKeyDown()) {
            packet = null;
            consumed = false;
        }

        if (packet != null && !consumed) {
            mc.player.connection.handlePlayerPosLook(packet);
            packet = null;
            consumed = false;
        }
    }
    @SubscribeEvent
    public void onItemUsed(ItemUsedEvent event) {
        if (event.getEntityLiving().equals(mc.player) && event.getStack().getItem().equals(Items.CHORUS_FRUIT)) {
            consumed = true;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
                if (event.getPacket() instanceof SPacketPlayerPosLook) {
                    event.setCanceled(true);
                }
                if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketConfirmTeleport) {
                    event.setCanceled(consumed);
                }
            }
        }

