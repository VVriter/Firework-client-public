package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Updater.Updater;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getClickSlot;

@ModuleManifest(name = "Surround", category = Module.Category.COMBAT)
public class Surround extends Module {
    public Setting<Boolean> shouldDisableOnJump = new Setting<>("DisableOnJump", true, this);
    public Setting<Double> keyClearDelay = new Setting<>("KeyDelay", 0.1d, this, 0, 3).setVisibility(shouldDisableOnJump, false);
    public Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);

    public Setting<centerModes> centerMode = new Setting<>("CMode", centerModes.Motion, this, centerModes.values()).setVisibility(shouldCenter, true);
    public enum centerModes{
        Motion, TP, NONE
    }

    public Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", false, this);
    public Setting<Integer> placeDelay = new Setting<>("PlaceDelayMs", 0, this, 0, 50);

    public Setting<switchModes> switchMode = new Setting<>("Switch", switchModes.Fast, this, switchModes.values());
    public enum switchModes{
        Fast, Silent
    }

    public Setting<Integer> tickDelay = new Setting<>("TickDelay", 0, this, 0, 60);

    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this);

    public Setting<InventoryUtil.hands> hand = new Setting<>("hand", InventoryUtil.hands.MainHand, this, InventoryUtil.hands.values());

    Timer placeTimer;
    Timer keyClearTimer;
    int lastKeyCode;
    ArrayList<BlockPos> line;

    public Surround(){
        line = new ArrayList<>();
        lastKeyCode = -1;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        //Registers new listener
        Firework.updaterManager.registerUpdater(listener1);

        //Clears old queue
        line.clear();

        //Setups & resets the place timer
        placeTimer = new Timer();
        placeTimer.reset();

        //Setups & resets the key clear timer
        keyClearTimer = new Timer();
        keyClearTimer.reset();

        //Moves player to a center
        if (shouldCenter.getValue()) {
            if (centerMode.getValue(centerModes.Motion)) {
                centerMotion();
            } else if(centerMode.getValue(centerModes.TP)) {
                centerTeleport();
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Firework.updaterManager.removeUpdater(listener1);
    }

    public Updater listener1 = new Updater(){
        @Override
        public void run() {
            super.run();
            if(mc.player == null || mc.world == null) return;

            this.delay = tickDelay.getValue();
            doSurroundPre();
        }
    };

    public void doSurroundPre() {
        List<BlockPos> blocksToPlace = getDynamicBlocksToPlaceList(new BlockPos(0, 0, 0));
        if (shouldToggle.getValue()){
            doSurroundInit(blocksToPlace);
            if(!containsAir(blocksToPlace))
                onDisable();
        }else{
            if(containsAir(blocksToPlace))
                doSurroundInit(blocksToPlace);
        }
    }

    //Places blocks from the list
    public void doSurroundInit(List<BlockPos> blocksToPlace){
        //Stops process if obby wasn't found in a hotbar
        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            return;
        }

        //Adds blocks to place to the queue
        line.addAll(blocksToPlace);

        //Deletes placed blocks from the queue
        for (int i = 0; i < line.size(); i++) {
            if (BlockUtil.getBlock(line.get(i)) != Blocks.AIR) {
                line.remove(i);
            }
        }

        //Returns if no blocks to place found
        if(line.isEmpty()) return;

        //Places blocks
        for(BlockPos blockPos : line){
            if(placeTimer.hasPassedMs(placeDelay.getValue())) {
                placeBlock(blockPos);
                placeTimer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            if(shouldDisableOnJump.getValue())
                onDisable();
            else
                /* Places surround blocks including jump offset */ doSurroundInit(getDynamicBlocksToPlaceList(new BlockPos(0, 1, 0)));
        }
    }

    @SubscribeEvent
    public void onPressedKey(InputEvent.KeyInputEvent event) {
        //Clears old pressed key value
        if(keyClearTimer.hasPassedS(keyClearDelay.getValue())) {
            lastKeyCode = Keyboard.getEventKey();
            keyClearTimer.reset();
        }
    }

    //Moves player to a center
    public void centerMotion() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};

        mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
        mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;
    }

    //Teleports player to a center
    public void centerTeleport() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        mc.player.connection.sendPacket(new CPacketPlayer.Position(centerPos[0], mc.player.posY, centerPos[2], mc.player.onGround));
        mc.player.setPosition(centerPos[0], mc.player.posY, centerPos[2]);
    }

    //BlockPos to place
    public void placeBlock(final BlockPos blockPos){
        //Return if block pos is null
        if(blockPos == null)
            return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(Blocks.OBSIDIAN), hands.MainHand);

        //Gets "enumHand" enum from "hands" enum
        EnumHand enumHand = hand.getValue(InventoryUtil.hands.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

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

    //Checks if player was centered
    public boolean isCentered() {
        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        return Math.abs(centerPos[0] - mc.player.posX) <= 0.1 && Math.abs(centerPos[2] - mc.player.posZ) <= 0.1;
    }

    //Gets fist layer of blocks to place
    public BlockPos[] getsFirstLayerOfBlocksToPlace(BlockPos offset) {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        return new BlockPos[]{p.add(1, 0, 0).add(offset), p.add(-1, 0, 0).add(offset), p.add(0, 0, 1).add(offset), p.add(0, 0, -1).add(offset)};
    }

    //Gets blocks to place by checking their neighbors
    public List<BlockPos> getDynamicBlocksToPlaceList(BlockPos offset){
        ArrayList<BlockPos> blocksToPlace = new ArrayList<>(Arrays.asList(getsFirstLayerOfBlocksToPlace(offset)));
        ArrayList<BlockPos> blocksToInsert = new ArrayList<>();
        for(int i = 0; i < blocksToPlace.size(); i++){
            BlockPos pos = blocksToPlace.get(i);
            if(BlockUtil.getPossibleSides(pos).isEmpty())
                blocksToInsert.add(pos.add(0, -1, 0));
        }
        blocksToInsert.addAll(blocksToPlace);
        return blocksToInsert;
    }

    //Checks if a blockpos array has any air blocks
    public boolean containsAir(List<BlockPos> blocks){
        for(BlockPos blockPos : blocks){
            if(BlockUtil.getBlock(blockPos) == Blocks.AIR){
                return true;
            }
        }
        return false;
    }
}
