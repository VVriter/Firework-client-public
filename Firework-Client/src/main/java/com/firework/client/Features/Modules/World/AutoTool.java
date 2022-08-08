package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.BlockClickEvent;
import com.firework.client.Implementations.Events.Player.PlayerDestroyBlockEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "AutoTool", category = Module.Category.WORLD)
public class AutoTool extends Module {

    BlockPos lastPos;
    static int lastSlot = -1;


    @Subscribe
    public Listener<BlockClickEvent> onPlayerHitBlock = new Listener<>(event -> {
        switchSlot(event.getPos());
        lastPos = event.getPos();
    });

    @Subscribe
    public Listener<PlayerDestroyBlockEvent> onPlayerDestroyBlock = new Listener<>(event -> {
        if(lastPos != null
                && lastPos.equals(event.getBlockPos())
                && lastSlot != -1){
            mc.player.inventory.currentItem = lastSlot;
            lastSlot = -1;
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (lastSlot != -1 && !mc.playerController.getIsHittingBlock()){
            mc.player.inventory.currentItem = lastSlot;
            lastSlot = -1;
        }
    });

    public static void switchSlot(final BlockPos blockPos) {
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        final Block block = BlockUtil.getBlock(blockPos);
        for (int i = 0; i < 9; ++i) {
            final ItemStack item = mc.player.inventory.getStackInSlot(i);
            if (item != null) {
                final float speed = item.getDestroySpeed(block.getBlockState().getBaseState());
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
        }
        if (bestSlot != -1 && mc.player.inventory.currentItem != bestSlot) {
            lastSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = bestSlot;
        }
    }

    public static int bestIntSlot(final BlockPos blockPos) {
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        final Block block = BlockUtil.getBlock(blockPos);
        for (int i = 0; i < 9; ++i) {
            final ItemStack item = mc.player.inventory.getStackInSlot(i);
            if (item != null) {
                final float speed = item.getDestroySpeed(block.getBlockState().getBaseState());
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
        }
        if (bestSlot != -1 && mc.player.inventory.currentItem != bestSlot) {
            return bestSlot;
        }
        return mc.player.inventory.getBestHotbarSlot();
    }
}
