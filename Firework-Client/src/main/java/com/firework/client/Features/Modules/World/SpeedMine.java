package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.BlockClickEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IPlayerControllerMP;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "SpeedMine", category = Module.Category.WORLD)
public class SpeedMine extends Module {

    public Setting<Integer> packetSpam = new Setting<>("PacketSpam", 1, this, 0, 10);

    BlockPos lastBlockPos;

    @Override
    public void onEnable() {
        super.onEnable();
        lastBlockPos = null;
    }

    @Subscribe
    public Listener<BlockClickEvent> blockClickEventListener = new Listener<>(blockClickEvent -> {
        if(fullNullCheck()) return;
        if(lastBlockPos != null || BlockUtil.isAir(lastBlockPos)){
            if(blockClickEvent.getPos().equals(lastBlockPos)) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, lastBlockPos, EnumFacing.DOWN));
                blockClickEvent.setCancelled(true);
                return;
            }else {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, lastBlockPos, blockClickEvent.getFacing()));
                ((IPlayerControllerMP) mc.playerController).setHitting(false);
                lastBlockPos = null;
            }
        }
        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        for (int j = 0; j < packetSpam.getValue(); j++) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockClickEvent.getPos(), blockClickEvent.getFacing()));
        }
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockClickEvent.getPos(), EnumFacing.DOWN));
        lastBlockPos = blockClickEvent.getPos();
        blockClickEvent.setCancelled(true);
    });
}
