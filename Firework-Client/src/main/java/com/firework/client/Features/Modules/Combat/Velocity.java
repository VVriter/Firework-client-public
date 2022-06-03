package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
    public Velocity(){super("Velocity",Category.COMBAT);}

    public Setting<Boolean> push = new Setting<>("Push", true, this);

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e){
        if (e.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity) e.getPacket()).getEntityID() == mc.player.getEntityId())
                e.setCanceled(true);
        }
        if (e.getPacket() instanceof SPacketExplosion)
            e.setCanceled(true);
    }

    @SubscribeEvent
    public void onPush(PlayerSPPushOutOfBlocksEvent e){
        if(push.getValue()){
        e.setCanceled(true);}
    }
}
