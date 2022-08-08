package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IEntity;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Crits",
        category = Module.Category.COMBAT,
        description = "Module to took more damage"
)
public class Criticals extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Packet, this);
    public enum modes{
        Packet, MiniJump, Jump
    }

    public Setting<Boolean> inWebToo = new Setting<>("InWebToo", true, this);
    public Setting<Boolean> killOnly = new Setting<>("AuraOnly", true, this);

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if (!killOnly.getValue()) {
            if (!(event.getPacket() instanceof CPacketUseEntity)) return;
            if (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK
                    && ((inWebToo.getValue() && !mc.player.onGround) ? (((IEntity) mc.player).isInWeb() ? true : false) : mc.player.onGround)) {
                if (mode.getValue(modes.Packet)) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.11, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1100013579, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.3579E-6, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer());
                } else if (mode.getValue(modes.MiniJump)) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                        mc.player.motionY = 0.25;
                    }
                } else if (mode.getValue(modes.Jump))
                    mc.player.jump();
            }
        } else if (killOnly.getValue() && Aura.enabled.getValue()) {
            if (!(event.getPacket() instanceof CPacketUseEntity)) return;
            if (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK
                    && ((inWebToo.getValue() && !mc.player.onGround) ? (((IEntity) mc.player).isInWeb() ? true : false) : mc.player.onGround)) {
                if (mode.getValue(modes.Packet)) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.11, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1100013579, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.3579E-6, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer());
                } else if (mode.getValue(modes.MiniJump)) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                        mc.player.motionY = 0.25;
                    }
                } else if (mode.getValue(modes.Jump))
                    mc.player.jump();
            }
        }
    });
}