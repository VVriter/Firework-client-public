package com.firework.client.Features.Modules.Combat.PistonCrystal;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

@ModuleManifest(name = "PistonAura",category = Module.Category.COMBAT)
public class PistonAura extends Module {

    EntityPlayer target;

    Timer placePistonTimer = new Timer();
    Timer placeCrystalTimer = new Timer();
    Timer placeRedstoneTimer = new Timer();

    @Override public void onEnable() { super.onEnable();

        //ItemsCheck
        if (!mc.player.inventory.hasItemStack(new ItemStack(Blocks.PISTON))
                || !mc.player.inventory.hasItemStack(new ItemStack(Blocks.REDSTONE_BLOCK))
                || !mc.player.inventory.hasItemStack(new ItemStack(Items.END_CRYSTAL))) {
            onDisable();
            MessageUtil.sendError("You need to has TORCH, REDSTONE_BLOCK/REDSTONE_TORCH, END_CRYSTAL in your inventory",-1117);
        }

        //TimersReset
        placePistonTimer.reset();
        placeCrystalTimer.reset();
        placeRedstoneTimer.reset();
    }

    @Override public void onDisable() { super.onDisable();
        //TimersReset
        placePistonTimer.reset();
        placeCrystalTimer.reset();
        placeRedstoneTimer.reset();
    }

    /********************************************SETTINGS**************************************/
    public Setting<Integer> targetRange = new Setting<>("TargetRange", 3, this, 1, 5);


    @Override public void onTick() { super.onTick();
        target = PlayerUtil.getClosestTarget(targetRange.getValue());
    }
















    public static int getPistonSlot() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockPistonBase) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }

    private int getRedstoneBlockSlot(){
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block == Blocks.REDSTONE_BLOCK || block == Blocks.REDSTONE_TORCH) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
}
