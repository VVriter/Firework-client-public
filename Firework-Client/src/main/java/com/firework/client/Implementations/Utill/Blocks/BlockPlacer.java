package com.firework.client.Implementations.Utill.Blocks;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
        //Returns if block pos is null
        if(blockPos == null) return;
        //Returns if block at the pos isn't valid
        if(!BlockUtil.isValid(blockPos)) return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(block1), InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

        if(switchMode.getValue(switchModes.Silent) && backSwitch != -1) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //BlockPos to place
    public void placeBlock(final BlockPos blockPos, final Block block1, boolean validCheck){
        //Returns if block pos is null
        if(blockPos == null) return;
        //Returns if block at the pos isn't valid
        if(validCheck && !BlockUtil.isValid(blockPos)) return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(block1), InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

        if(switchMode.getValue(switchModes.Silent) && backSwitch != -1) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //BlockPos to place
    public void placeBlockEnumFacing(final BlockPos blockPos, final EnumFacing lookFacing, final Block block1){
        //Returns if block pos is null
        if(blockPos == null) return;
        //Returns if block at the pos isn't valid
        if(!BlockUtil.isValid(blockPos)) return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(block1), InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        BlockUtil.placeBlockLookFacing(blockPos, lookFacing, enumHand, rotate.getValue(), packet.getValue(), BlockUtil.blackList.contains(BlockUtil.getBlock(blockPos.add(0, -1, 0))) ? true : false);

        if(switchMode.getValue(switchModes.Silent) && backSwitch != -1) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //Place trap block
    public void placeTrapBlock(final BlockPos blockPos, final Block block1){
        //Returns if block pos is null
        if(blockPos == null) return;
        //Returns if block at the pos isn't valid
        if(!BlockUtil.isValid(blockPos)) return;

        //Switchs
        int backSwitch = switchItems(Item.getItemFromBlock(block1), InventoryUtil.hands.MainHand);

        //Gets "enumHand" enum
        EnumHand enumHand = EnumHand.MAIN_HAND;

        //Places block
        trapBlock(blockPos);

        if(switchMode.getValue(switchModes.Silent) && backSwitch != -1) {
            switchItems(getItemStack(backSwitch).getItem(), InventoryUtil.hands.MainHand);
        }
    }

    //PlaceTrap block
    public static void trapBlock(BlockPos pos)
    {
        for (EnumFacing enumFacing : EnumFacing.values())
        {
            if (!mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos))
            {
                Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getZOffset() * 0.5D);

                float[] old = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};

                mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - mc.player.posZ), (vec.x - mc.player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (mc.player.posY + (double) mc.player.getEyeHeight())), (Math.sqrt((vec.x - mc.player.posX) * (vec.x - mc.player.posX) + (vec.z - mc.player.posZ) * (vec.z - mc.player.posZ)))))), mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], mc.player.onGround));

                return;
            }
        }
    }
    public static boolean isIntercepted(BlockPos pos)
    {
        for (Entity entity : mc.world.loadedEntityList)
        {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }

        return false;
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
