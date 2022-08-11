package com.firework.client.Implementations.Utill.Items;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Blocks.PredictPlace;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getClickSlot;
import static com.firework.client.Implementations.Utill.Util.mc;

public class ItemUser {
    private Module module;
    private Setting<ItemUser.switchModes> switchMode;
    private Setting<Boolean> rotate;
    private Setting<Boolean> packet;
    private Setting<Boolean> swing;

    public ItemUser(Module module, Setting switchMode, Setting rotate){
        this.module = module;
        this.switchMode = switchMode;
        this.rotate = rotate;
    }

    public ItemUser(Module module, Setting switchMode, Setting rotate, Setting packet, Setting swing){
        this.module = module;
        this.switchMode = switchMode;
        this.rotate = rotate;
        this.packet = packet;
        this.swing = swing;
    }

    //Uses item
    public void useItem(final Item item, final int pitch){

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

        if(switchMode.getValue(ItemUser.switchModes.Silent) && backSwitch != -1) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //Uses item
    public void useItem(final Item item, final BlockPos blockPos, final EnumHand hand){

        //Switchs
        int backSwitch = switchItems(item, InventoryUtil.hands.MainHand);

        if (rotate.getValue()) {
            Firework.rotationManager.rotateSpoof(new Vec3d(blockPos.getX() + 0.5, blockPos.getY() - 0.5, blockPos.getZ() + 0.5));
        }

        //Uses item
        EnumFacing facing = EnumFacing.UP;
        PredictPlace result = BlockUtil.getFacingToClick(blockPos);

        if (result != null)
            facing = result.getFacing();

        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, facing, hand, 0, 0,0));


        if(switchMode.getValue(ItemUser.switchModes.Silent) && backSwitch != -1) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //Hits an entity
    public void hitEntity(final Item item, EntityLivingBase entityLivingBase){

        //Switchs
        int backSwitch = switchItems(item, InventoryUtil.hands.MainHand);

        if (rotate.getValue()) {
            Firework.rotationManager.rotateSpoof(entityLivingBase.getPositionVector().add(0, entityLivingBase.getEyeHeight(), 0));
        }

        EntityUtil.attackEntity(entityLivingBase, packet.getValue(), swing.getValue());

        if(switchMode.getValue(ItemUser.switchModes.Silent) && backSwitch != -1) {
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

    //Switch modes
    public enum switchModes{
        Fast, Silent
    }
}
