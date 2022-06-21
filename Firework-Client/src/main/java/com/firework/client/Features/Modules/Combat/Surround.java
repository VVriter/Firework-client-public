package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;

@ModuleArgs(name = "Surround", category = Module.Category.COMBAT)
public class Surround extends Module {

    public Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);
    public Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", true, this);

    public Setting<Boolean> shuldDisableOnJump = new Setting<>("DisableOnJump", true, this);
    public Setting<Integer> tickDelay = new Setting<>("TickDelay", 0, this, 0, 20).setVisibility(shouldToggle, false);

    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this);

    public Setting<Boolean> useEnderChestIfNoObsFound = new Setting<>("UseEChestAsReserv", true, this);

    public Setting<hands> hand = new Setting<>("hand", hands.MainHand, this, hands.values());

    @Override
    public void onEnable() {
        super.onEnable();
        if(shouldCenter.getValue())
            center();

        surround();

        if(shouldToggle.getValue())
            onDisable();
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        delay = tickDelay.getValue().intValue();
        if(mc.player.inventory.hasItemStack(new ItemStack(Blocks.OBSIDIAN))
                | (useEnderChestIfNoObsFound.getValue() ? (mc.player.inventory.hasItemStack(new ItemStack(Blocks.ENDER_CHEST)) ? true : false) : false)) {
            for (BlockPos blockPos : getBlocksToPlace()) {
                if (BlockUtil.getBlock(blockPos) == Blocks.AIR) {
                    surround();
                    return;
                }
            }
        }
    }


    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            if(shuldDisableOnJump.getValue()){onDisable();}
        }
    }

    public void surround(){
        EnumHand enumHand = hand.getValue(hands.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;

        Item oldItem = getItemStack(hand.getValue(hands.MainHand) ? (36 + mc.player.inventory.currentItem) : 45).getItem();

        doMultiHand(mc.player.inventory.hasItemStack(new ItemStack(Blocks.OBSIDIAN)) ? Item.getItemFromBlock(Blocks.OBSIDIAN) :
                (useEnderChestIfNoObsFound.getValue() ? Item.getItemFromBlock(Blocks.ENDER_CHEST) : null), hand.getValue());

        boolean shouldSneak = BlockUtil.getBlock(getPlayerPos().add(0, -1, 0)).equals(Blocks.ENDER_CHEST) ? true : false;

        for(BlockPos pos : getBlocksToPlace()){
            if(BlockUtil.getBlock(pos) == Blocks.AIR)
                BlockUtil.placeBlock(pos, enumHand, rotate.getValue(), packet.getValue(), shouldSneak);
        }
        doMultiHand(oldItem, hand.getValue());
    }

    public void center() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};

        mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
        mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;
    }

    public boolean isCentered() {
        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        return Math.abs(centerPos[0] - mc.player.posX) <= 0.1 && Math.abs(centerPos[2] - mc.player.posZ) <= 0.1;
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Util.mc.player.posX), Math.floor(Util.mc.player.posY), Math.floor(Util.mc.player.posZ));
    }

    public BlockPos[] getBlocksToPlace() {
        BlockPos p = getPlayerPos();
        return new BlockPos[]{p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1), p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
    }
}
