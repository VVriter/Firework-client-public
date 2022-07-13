package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IEntity;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "Crits", category = Module.Category.COMBAT)
public class Criticals extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Packet, this, modes.values());
    public enum modes{
       Packet, MiniJump
    }

    public Setting<Boolean> inWebToo = new Setting<>("InWebToo", true, this);


    //PacketMode
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        CPacketUseEntity packet;

        if (mode.getValue(modes.Packet)) {
            if (inWebToo.getValue() && ((IEntity)mc.player).isInWeb()) {
                if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && (!(event.getPacket() instanceof EntityEnderCrystal))) {
                    if (((CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
                    Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.11, Criticals.mc.player.posZ, false));
                    Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1100013579, Criticals.mc.player.posZ, false));
                    Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.3579E-6, Criticals.mc.player.posZ, false));
                    Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
                    }
                }
            } else  if (!inWebToo.getValue() && !((IEntity)mc.player).isInWeb()) {
                if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && (!(event.getPacket() instanceof EntityEnderCrystal))) {
                    if (((CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.11, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1100013579, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.3579E-6, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
                    }
                }
            }
        }



        if (mode.getValue(modes.MiniJump)) {
            if (inWebToo.getValue() && ((IEntity)mc.player).isInWeb()) {
                if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && (!(event.getPacket() instanceof EntityEnderCrystal))) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                        mc.player.motionY = 0.25;
                        }
                    }
                } else if (!inWebToo.getValue() && !((IEntity)mc.player).isInWeb()) {
                if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && (!(event.getPacket() instanceof EntityEnderCrystal))) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                        mc.player.motionY = 0.25;
                    }
                }
            }
        }
    }
}
