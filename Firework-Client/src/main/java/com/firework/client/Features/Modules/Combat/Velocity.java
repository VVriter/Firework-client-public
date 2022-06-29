package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "Velocity", category = Module.Category.COMBAT)
public class Velocity extends Module {


    public Setting<Boolean> Velocity = new Setting<>("Velocity", true, this);
    public Setting<Boolean> Explosion = new Setting<>("Explosion", true, this);
    public Setting<Boolean> push = new Setting<>("Push", true, this);

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e){
        if (e.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity) e.getPacket()).getEntityID() == mc.player.getEntityId())
                e.setCanceled(Velocity.getValue());
        }
        if (e.getPacket() instanceof SPacketExplosion)
            e.setCanceled(Explosion.getValue());
    }


    @SubscribeEvent
    public void onPush(PlayerSPPushOutOfBlocksEvent e){
        e.setCanceled(push.getValue());
    }
}
