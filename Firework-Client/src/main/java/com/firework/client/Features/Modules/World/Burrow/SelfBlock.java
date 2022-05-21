package com.firework.client.Features.Modules.World.Burrow;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class SelfBlock extends Module {

    public Setting<Double> offset  = new Setting<>("Offset", (double)3, this, -5, 5);
    public Setting<Boolean> ground  = new Setting<>("Ground check", false, this);
    public Setting<Boolean> rotate  = new Setting<>("Rotate", false, this);
    public Setting<Boolean> echest   = new Setting<>("Use echest", false, this);
    public Setting<Boolean> anvil   = new Setting<>("Use anvil", false, this);


    public SelfBlock(){super("SelfBlock",Category.WORLD);}

    private BlockPos originalPos;
    private int oldSlot = -1;

    @Override
    public void onEnable() {
        super.onEnable();

        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
                intersectsWithEntity(this.originalPos)) {
            this.isEnabled.setValue(false);
            return;
        }

        oldSlot = mc.player.inventory.currentItem;
    }


    public void onTick(){
        super.onTick();
        if (ground.getValue()) {
            if (!mc.player.onGround) {
                this.isEnabled.setValue(false);
                return;
            }
        }

        if (anvil.getValue() && BurrowUtil.findHotbarBlock(BlockAnvil.class) != -1) {
            BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockAnvil.class));
        } else if (echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1 : BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            BurrowUtil.switchToSlot(echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockObsidian.class));
        } else {
            MessageUtil.sendClientMessage("Unable to place burrow block (anvil, ec or oby)",true);
            this.isEnabled.setValue(false);
            return;
        }

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));

        BurrowUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));

        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        mc.player.setSneaking(false);

        BurrowUtil.switchToSlot(oldSlot);

        this.isEnabled.setValue(false);
    }



    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }

}
