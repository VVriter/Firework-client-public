package com.firework.client.Implementations.Utill.Items;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getClickSlot;
import static com.firework.client.Implementations.Utill.Util.mc;

public class ItemUser {
    private Module module;
    private Setting<ItemUser.switchModes> switchMode;
    private Setting<Boolean> rotate;

    public ItemUser(Module module, Setting switchMode, Setting rotate){
        this.module = module;
        this.switchMode = switchMode;
        this.rotate = rotate;
    }

    //Uses item
    public void useItem(final Item item, final int pitch){
        //Updates local settings
        updateSettings();

        //Switchs
        int backSwitch = switchItems(item, InventoryUtil.hands.MainHand);

        //Uses item
        int oldPitch = (int) mc.player.rotationPitch;
        if (rotate.getValue()) {
            mc.player.rotationPitch = pitch;
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, pitch, true));
        }
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        if (rotate.getValue()) {
            mc.player.rotationPitch = oldPitch;
        }

        if(switchMode.getValue(ItemUser.switchModes.Silent)) {
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
                }else
                    return -1;
                return prevSlot;
            }else{
                return -1;
            }
        }
        return 0;
    }

    //Switches hotbar active slot
    public static void switchHotBarSlot(int slot){
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

    //Update settings
    private void updateSettings(){
        this.switchMode = Firework.settingManager.getSetting(module, switchMode.name);
        this.rotate = Firework.settingManager.getSetting(module, rotate.name);
    }

    //Switch modes
    public enum switchModes{
        Fast, Silent
    }
}
