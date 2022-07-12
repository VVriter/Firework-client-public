package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "NoSlowRewrite", category = Module.Category.MOVEMENT)
public class NoSlowRewrite extends Module {

    private Setting<Boolean> strict = new Setting<>("Strict", true, this);
    private Setting<Boolean> items = new Setting<>("Items", true, this);

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (items.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe /= 0.2f;
            event.getMovementInput().moveForward /= 0.2f;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketPlayer && strict.getValue() && items.getValue()
                && mc.player.isHandActive() && !mc.player.isRiding()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, EntityUtil.getFlooredPos(mc.player), EnumFacing.DOWN));
        }
    }
}
