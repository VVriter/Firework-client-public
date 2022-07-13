package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "Criticals", category = Module.Category.COMBAT)
public class Criticals extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Packet, this, modes.values());
    public enum modes{
       Packet
    }



    //PacketMode
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (mode.getValue(modes.Packet)) {
         if (event.getPacket() instanceof CPacketUseEntity && (!(event.getPacket() instanceof EntityEnderCrystal))) {
            if (((CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
                Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.11, Criticals.mc.player.posZ, false));
                Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1100013579, Criticals.mc.player.posZ, false));
                Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.3579E-6, Criticals.mc.player.posZ, false));
                Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
                }
            }
        }
    }
}
