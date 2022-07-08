package com.firework.client.Implementations.Utill.Blocks;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getClickSlot;

public class BlockPlacer {
    private Module module;
    private Block block;
    private Setting<switchModes> switchMode;
    private Setting<Boolean> rotate;
    private Setting<Boolean> packet;

    public BlockPlacer(Module module, Block targetBlock, Setting switchMode, Setting rotate, Setting packet){
        this.module = module;
        this.block = targetBlock;
        this.switchMode = switchMode;
        this.rotate = rotate;
        this.packet = packet;
    }

    public BlockPlacer(Module module, Setting switchMode, Setting rotate, Setting packet){
        this.module = module;
        this.switchMode = switchMode;
        this.rotate = rotate;
        this.packet = packet;
    }

    //BlockPos to place
    public void placeBlock(final BlockPos blockPos){
        //Updates local settings
        updateSettings();
        //Return if block pos is null
        if(blockPos == null)
            return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(block), InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

        if(switchMode.getValue(switchModes.Silent)) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //BlockPos to place
    public void placeBlock(final BlockPos blockPos, final Block block1){
        //Updates local settings
        updateSettings();
        //Return if block pos is null
        if(blockPos == null)
            return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(block1), InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

        if(switchMode.getValue(switchModes.Silent)) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //Switches to needed item
    private int switchItems(Item item, InventoryUtil.hands hand){
        if(hand == InventoryUtil.hands.MainHand){
            if(getClickSlot(getItemSlot(item)) !=  getClickSlot(mc.player.inventory.currentItem)){
                int prevSlot = mc.player.inventory.currentItem;
                if (getHotbarItemSlot(item) != -1) {
                    switchHotBarSlot(getHotbarItemSlot(item));
                } else {
                    swapSlots(getItemSlot(item), getHotbarItemSlot(Item.getItemFromBlock(Blocks.AIR)));
                    switchHotBarSlot(getHotbarItemSlot(item));
                }
                return prevSlot;
            }else{
                return -1;
            }
        }
        return 0;
    }

    //Switches hotbar active slot
    private void switchHotBarSlot(int slot){
        InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        InventoryUtil.mc.player.inventory.currentItem = slot;
        InventoryUtil.mc.playerController.updateController();
    }

    //Swaps items between 2 slots
    private void swapSlots(int from, int to){
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(to), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
    }

    //Update settings
    private void updateSettings(){
        this.switchMode = Firework.settingManager.getSetting(module, switchMode.name);
        this.rotate = Firework.settingManager.getSetting(module, rotate.name);
        this.packet = Firework.settingManager.getSetting(module, packet.name);
    }

    //Switch modes
    public enum switchModes{
        Fast, Silent
    }
}
