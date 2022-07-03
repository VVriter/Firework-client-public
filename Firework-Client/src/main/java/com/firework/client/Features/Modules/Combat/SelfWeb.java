package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getClickSlot;

@ModuleManifest(name = "SelfWeb", category = Module.Category.COMBAT)
public class SelfWeb extends Module {

    //Should toggle onEnable
    private Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", false, this);
    //Should toggle on pos change
    private Setting<Boolean> toggleOnMove = new Setting<>("ToggleOnMove", true, this);

    //Should place web only if predicted that Target wants to get into the hole
    private Setting<Boolean> predictTargetMove = new Setting<>("PredictPlace", true, this);

    //Should rotate on block place
    private Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    //Should send packet confirmation on block place
    private Setting<Boolean> packet = new Setting<>("Packet", true, this);
    //Switch mode
    private Setting<switchModes> switchMode = new Setting<>("Switch", switchModes.Fast, this, switchModes.values());
    private enum switchModes{
        Fast, Silent
    }

    //Last block pos resets onDisable and is being used to check did player move or not
    private BlockPos lastBlockPos = null;

    //Target
    private EntityPlayer target = null;

    @Override
    public void onEnable() {
        super.onEnable();
        //Resets last block pos
        lastBlockPos = null;
    }

    @Override
    public void onTick() {
        super.onTick();

        //Stops process if web wasn't found in a hotbar
        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.WEB)) == -1) {
            MessageUtil.sendError("No web found in the hotbar", -1117);
            return;
        }

        //Sets last block pos
        if(lastBlockPos == null)
            lastBlockPos = EntityUtil.getFlooredPos(mc.player);

        //Turns off if player moved to another block
        if(toggleOnMove.getValue()) {
            if (!EntityUtil.getFlooredPos(mc.player).equals(lastBlockPos)) {
                onDisable();
                return;
            }
        }

        target = PlayerUtil.getClosest();
        //Places web at ur feet
        if(predictTargetMove.getValue()) {
            if(target == null) return;
            if(target.posY - mc.player.posY > 0.25 && Math.abs(mc.player.posX - target.posX) < 2 && Math.abs(mc.player.posZ - target.posZ) < 2) {
                if (BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player)) != Blocks.WEB)
                    placeBlock(EntityUtil.getFlooredPos(mc.player));
            }
        }else{
            if (BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player)) != Blocks.WEB)
                placeBlock(EntityUtil.getFlooredPos(mc.player));
        }

        //Turns off if should
        if(shouldToggle.getValue())
            onDisable();
    }

    //BlockPos to place
    public void placeBlock(final BlockPos blockPos){
        //Return if block pos is null
        if(blockPos == null)
            return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(Blocks.WEB), hands.MainHand);

        //Gets "enumHand" enum from "hands" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

        //Do silent switch
        if(switchMode.getValue(switchModes.Silent)) {
            switchItems(getItemStack(backSwitch).getItem(), hands.MainHand);
        }
    }

    //Switches to needed item
    public int switchItems(Item item, InventoryUtil.hands hand){
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
    public void switchHotBarSlot(int slot){
        InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        InventoryUtil.mc.player.inventory.currentItem = slot;
        InventoryUtil.mc.playerController.updateController();
    }

    //Swaps items between 2 slots
    public static void swapSlots(int from, int to){
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(to), 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, getClickSlot(from), 0, ClickType.PICKUP, mc.player);
    }
}
