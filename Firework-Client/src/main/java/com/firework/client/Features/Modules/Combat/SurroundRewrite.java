package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;

@ModuleManifest(name = "SurroundRewrite", category = Module.Category.COMBAT)
public class SurroundRewrite extends Module{
    public Setting<Boolean> shouldDisableOnJump = new Setting<>("DisableOnJump", false, this);
    public Setting<Double> keyClearDelay = new Setting<>("KeyDelayS", 0.3d, this, 0, 3).setVisibility(shouldDisableOnJump, false);
    public Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);

    public Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", false, this);
    public Setting<Double> placeDelay = new Setting<>("PlaceDelayS", 0d, this, 0, 20).setVisibility(shouldToggle, false);

    public Setting<switchModes> switchMode = new Setting<>("Switch", switchModes.Fast, this, switchModes.values());
    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this);

    public Setting<Boolean> useEnderChestIfNoObsFound = new Setting<>("UseEChestAsReserv", true, this);

    public Setting<hands> hand = new Setting<>("hand", hands.MainHand, this, hands.values());

    Timer placeTimer;
    Timer keyClearTimer;
    int lastKeyCode;
    ArrayList<BlockPos> line;

    public SurroundRewrite(){
        line = new ArrayList<>();
        lastKeyCode = -1;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        placeTimer = new Timer();
        placeTimer.reset();

        keyClearTimer = new Timer();
        keyClearTimer.reset();

        if (shouldCenter.getValue())
            center();

        switchItems(Item.getItemFromBlock(Blocks.OBSIDIAN), hands.MainHand, switchMode.getValue());

        if (shouldToggle.getValue()){
            doSurround(getBlocksToPlace());
            onDisable();
        }
    }

    @Override
    public void onTick() {
        super.onTick();
        if(shouldCenter.getValue())
            center();

        doSurround(getBlocksToPlace());
    }

    public void doSurround(BlockPos[] blocksToPlace){

        EnumHand enumHand = hand.getValue(InventoryUtil.hands.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;

        int switchBack = -1;
        Item oldItem = null;

        if(lastKeyCode == mc.gameSettings.keyBindJump.getKeyCode()) {
            oldItem = getItemStack(36 + mc.player.inventory.currentItem).getItem();
            switchBack = switchItems(Item.getItemFromBlock(Blocks.OBSIDIAN), hands.MainHand, switchMode.getValue());

            BlockUtil.placeBlock(EntityUtil.getFlooredPos(mc.player).add(0, -1, 0), enumHand, rotate.getValue(), packet.getValue(), true);
            lastKeyCode = -1;

            if(switchBack == 0)
                switchItems(oldItem, hands.MainHand, switchMode.getValue());

            switchBack = -1;
            oldItem = null;
        }

        if(!placeDelay.getValue(0d)) {
            for (int i = 0; i < line.size(); i++) {
                if (BlockUtil.getBlock(line.get(i)) != Blocks.AIR)
                    line.remove(i);
            }

            for (BlockPos blockPos : blocksToPlace) {
                if (BlockUtil.getBlock(blockPos) == Blocks.AIR) {
                    line.add(blockPos);
                }
            }

            if(!line.isEmpty() && placeTimer.passedS(placeDelay.getValue())){
                oldItem = getItemStack(36 + mc.player.inventory.currentItem).getItem();
                switchBack = switchItems(Item.getItemFromBlock(Blocks.OBSIDIAN), hands.MainHand, switchMode.getValue());
            }

            for (BlockPos blockPos : line){
                if (placeTimer.passedS(placeDelay.getValue())) {
                    BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), true);
                    placeTimer.reset();
                }
            }

            if(switchBack == 0){
                switchItems(oldItem, hands.MainHand, switchMode.getValue());

                switchBack = -1;
                oldItem = null;
            }
        }else{
            if(containsAir(getBlocksToPlace())) {
                if (!Arrays.asList(getBlocksToPlace()).isEmpty()) {
                    oldItem = getItemStack(36 + mc.player.inventory.currentItem).getItem();
                    switchBack = switchItems(Item.getItemFromBlock(Blocks.OBSIDIAN), hands.MainHand, switchMode.getValue());
                }

                for (BlockPos blockPos : getBlocksToPlace()) {
                    BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), true);
                }

                if(switchBack == 0){
                    switchItems(oldItem, hands.MainHand, switchMode.getValue());

                    switchBack = -1;
                    oldItem = null;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            if(shouldDisableOnJump.getValue())
                onDisable();
            else
                doSurround(getBlocksToPlace(new BlockPos(0, 1, 0)));
        }
    }

    @SubscribeEvent
    public void onPressedKey(InputEvent.KeyInputEvent event) {
        if(keyClearTimer.passedS(keyClearDelay.getValue())) {
            lastKeyCode = Keyboard.getEventKey();
            keyClearTimer.reset();
        }
    }

    public int switchItems(Item item, hands hand, switchModes mode){
        if(hand == hands.MainHand){
            if(getItemSlot(item) !=  (36 + mc.player.inventory.currentItem)){
                System.out.println(getItemSlot(item) + "|" + (36 + mc.player.inventory.currentItem));
                switch(mode){
                    case Fast:
                        if(getHotbarItemSlot(item) != -1){
                            switchHotBarSlot(getHotbarItemSlot(item));
                        }else{
                            swapSlots(getItemSlot(item), getHotbarItemSlot(Item.getItemFromBlock(Blocks.AIR)));
                            switchHotBarSlot(getHotbarItemSlot(item));
                        }
                        break;
                }
            }else{
                return -1;
            }
        }
        return 0;
    }

    public void switchHotBarSlot(int slot){
        InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        InventoryUtil.mc.player.inventory.currentItem = slot;
        InventoryUtil.mc.playerController.updateController();
    }

    public void swapSlots(int from, int to){
        mc.playerController.windowClick(0, from, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, to, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, from, 0, ClickType.PICKUP, mc.player);
    }

    public void center() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};

        mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
        mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;
    }

    public enum switchModes{
        Fast, Universal, Silent
    }

    public boolean isCentered() {
        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        return Math.abs(centerPos[0] - mc.player.posX) <= 0.1 && Math.abs(centerPos[2] - mc.player.posZ) <= 0.1;
    }

    public BlockPos[] getBlocksToPlace() {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        return new BlockPos[]{p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1), p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
    }

    public BlockPos[] getBlocksToPlace(BlockPos offset) {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        return new BlockPos[]{p.add(1, -1, 0).add(offset), p.add(-1, -1, 0).add(offset), p.add(0, -1, 1).add(offset), p.add(0, -1, -1).add(offset), p.add(1, 0, 0).add(offset), p.add(-1, 0, 0).add(offset), p.add(0, 0, 1).add(offset), p.add(0, 0, -1).add(offset)};
    }

    public boolean containsAir(BlockPos[] blocks){
        for(BlockPos blockPos : blocks){
            if(BlockUtil.getBlock(blockPos) == Blocks.AIR){
                return true;
            }
        }
        return false;
    }
}
