package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "AutoTool",category = Module.Category.MISC)
public class AutoTool extends Module {
    public Setting<Enum> mode = new Setting<>("Mode", modes.Normal, this, modes.values());
    public enum modes {
        Normal, MultiHand, Silent
    }


    @SubscribeEvent
    public void on (PlayerInteractEvent.LeftClickBlock e) {
        switchSlot(e.getPos());
    }

    public void switchSlot (final BlockPos blockPos) {
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        final Block block = mc.world.getBlockState(blockPos).getBlock();
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
        if (bestSlot != -1) {
            if (mode.getValue(modes.Normal)) {
            mc.player.inventory.currentItem = bestSlot;
            } else if (mode.getValue(modes.MultiHand)){
                InventoryUtil.doMultiHand(36+bestSlot,InventoryUtil.hands.MainHand);
            }
        }
    }
}
