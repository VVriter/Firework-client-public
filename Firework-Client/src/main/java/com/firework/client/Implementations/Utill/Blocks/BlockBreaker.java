package com.firework.client.Implementations.Utill.Blocks;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getClickSlot;
import static com.firework.client.Implementations.Utill.Util.mc;

public class BlockBreaker {
    private Module module;
    private Setting<mineModes> mineMode;
    private Setting<Boolean> rotate;
    private Setting<Boolean> rayTrace;
    private Setting<Boolean> packet;

    private boolean firstHit = true;

    public BlockBreaker(Module module, Setting switchMode, Setting rotate, Setting rayTrace, Setting packet){
        this.module = module;
        this.mineMode = switchMode;
        this.rotate = rotate;
        this.rayTrace = rayTrace;
        this.packet = packet;
    }


    //Breaks block
    public void breakBlock(final BlockPos blockPos, final Item item){
        //Updates local settings
        updateSettings();
        //Return if block pos is null
        if(blockPos == null)
            return;

        //Switchs
        switchItems(item, InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Rotates
        if(rotate.getValue()) {
            Vec3d hitVec = new Vec3d(blockPos).add(0.5, 0.5, 0.5);
            RotationUtil.rotate(hitVec, packet.getValue());
        }

        //Gets facing
        EnumFacing facing = EnumFacing.UP;
        if (rayTrace.getValue()) {
            RayTraceResult result = mc.world.rayTraceBlocks(
                    new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
                    new Vec3d(blockPos.getX() + 0.5, blockPos.getY() - 0.5,
                            blockPos.getZ() + 0.5));

            if (result != null && result.sideHit != null)
                facing = result.sideHit;
        }

        //Breaks block
        if(mineMode.getValue(mineModes.Packet)) {
            mc.player.swingArm(enumHand);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, facing));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    blockPos, facing));
        }else if(mineMode.getValue(mineModes.Classic)){
            if(firstHit){
                firstHit = false;
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, facing));
            }else {
                mc.playerController.onPlayerDamageBlock(blockPos, facing);
            }
            if(BlockUtil.getBlock(blockPos) == Blocks.AIR)
                firstHit = true;
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
        this.mineMode = Firework.settingManager.getSetting(module, mineMode.name);
        this.rotate = Firework.settingManager.getSetting(module, rotate.name);
        this.rayTrace = Firework.settingManager.getSetting(module, rayTrace.name);
        this.packet = Firework.settingManager.getSetting(module, packet.name);
    }

    //Mine modes
    public enum mineModes {
        Classic, Packet
    }
}
