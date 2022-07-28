package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Movement.PlayerPushOutOfBlocksEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Mixins.MixinsList.ISPacketPlayerPosLook;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.*;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "SelfBlock",
        category = Module.Category.WORLD,
        description = "Places block in your feet"
)
public class Burrow extends Module {

    public Setting<Boolean> main = new Setting<>("Main", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> main.getValue());
    public Setting<Boolean> swing = new Setting<>("Swing", true, this).setVisibility(v-> main.getValue());
    public Setting<Boolean> strict = new Setting<>("Bypass", true, this).setVisibility(v-> main.getValue());


    public Setting<Boolean> blocks = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> skulls = new Setting<>("Skulls", true, this).setVisibility(v-> blocks.getValue());
    public Setting<Boolean> onlyEChest = new Setting<>("EChestOnly", false, this).setVisibility(v-> blocks.getValue());


    private State state = State.WAITING;
    private Timer timer = new Timer();

    private enum State {
        WAITING,
        DISABLING
    }

    @Override
    public void onTick() {
        super.onTick();
        if (mc.player == null || mc.world == null) return;
        if (state == State.DISABLING) {
            if (timer.hasPassedMs(500)) {
                toggle();
            }
            return;
        }
        if (!mc.player.onGround) {
            toggle();
            return;
        }
        if (mc.world.getBlockState(new BlockPos(mc.player)).getBlock() == Blocks.AIR) {
            if (skulls.getValue() && mc.world.getBlockState(new BlockPos(mc.player).up(2)).getBlock() != Blocks.AIR) {

                if (getHeadSlot() == -1) {
                    System.out.println("head slot -1");
                    toggle();
                    return;
                }

                BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

                BlockPos currentPos = pos.down();
                EnumFacing currentFace = EnumFacing.UP;

                Vec3d vec = new Vec3d(currentPos)
                        .add(0.5, 0.5, 0.5)
                        .add(new Vec3d(currentFace.getDirectionVec()).scale(0.5));

                if (rotate.getValue()) {
                    if (((IEntityPlayerSP) mc.player).getLastReportedPitch() < 0) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 0, true));
                    }
                    mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, 90, true));            ((IEntityPlayerSP) mc.player).setLastReportedPosY(mc.player.posY + 1.16);
                    ((IEntityPlayerSP) mc.player).setLastReportedPitch(90);
                }

                float f = (float) (vec.x - (double) pos.getX());
                float f1 = (float) (vec.y - (double) pos.getY());
                float f2 = (float) (vec.z - (double) pos.getZ());

                boolean changeItem = mc.player.inventory.currentItem != getHeadSlot();
                int startingItem = mc.player.inventory.currentItem;

                if (changeItem) {
                    mc.player.inventory.currentItem = getHeadSlot();
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(getHeadSlot()));
                }

                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(currentPos, currentFace, EnumHand.MAIN_HAND, f, f1, f2));
                if (swing.getValue()) {
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }

                if (changeItem) {
                    mc.player.inventory.currentItem = startingItem;
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(startingItem));
                }

                System.out.println("mmm");
                timer.reset();
                state = State.DISABLING;
                return;
            }

            if (getBlockSlot() == -1) {
                toggle();
                return;
            }

            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

            BlockPos currentPos = pos.down();
            EnumFacing currentFace = EnumFacing.UP;

            Vec3d vec = new Vec3d(currentPos)
                    .add(0.5, 0.5, 0.5)
                    .add(new Vec3d(currentFace.getDirectionVec()).scale(0.5));

            if (rotate.getValue()) {
                if (((IEntityPlayerSP) mc.player).getLastReportedPitch() < 0) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 0, true));
                }
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, 90, true));            ((IEntityPlayerSP) mc.player).setLastReportedPosY(mc.player.posY + 1.16);
                ((IEntityPlayerSP) mc.player).setLastReportedPitch(90);
            }

            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.75, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.01, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16, mc.player.posZ, mc.player.onGround));

            float f = (float) (vec.x - (double) pos.getX());
            float f1 = (float) (vec.y - (double) pos.getY());
            float f2 = (float) (vec.z - (double) pos.getZ());

            boolean changeItem = mc.player.inventory.currentItem != getBlockSlot();
            int startingItem = mc.player.inventory.currentItem;

            if (changeItem) {
                mc.player.inventory.currentItem = getBlockSlot();
                mc.player.connection.sendPacket(new CPacketHeldItemChange(getBlockSlot()));
            }

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(currentPos, currentFace, EnumHand.MAIN_HAND, f, f1, f2));
            if (swing.getValue()) {
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            }

            if (changeItem) {
                mc.player.inventory.currentItem = startingItem;
                mc.player.connection.sendPacket(new CPacketHeldItemChange(startingItem));
            }

            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, getPos(), mc.player.posZ, false));
            timer.reset();
            state = State.DISABLING;
        } else {
            toggle();
        }
    }



    private double getPos() {
        if (mc.getCurrentServerData() != null) {
            if (mc.getCurrentServerData().serverIP.toLowerCase().contains("crystalpvp")) {
                return mc.player.posY + 1.8D + (Math.random() * 0.1);
            } else if (mc.getCurrentServerData().serverIP.toLowerCase().contains("endcrystal")) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 4D, mc.player.posZ)).getBlock() instanceof BlockAir) {
                    return mc.player.posY + 4D;
                }
                return mc.player.posY + 3D;
            } else if (mc.getCurrentServerData().serverIP.toLowerCase().contains("netheranarchy")) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 8.5D, mc.player.posZ)).getBlock() instanceof BlockAir) {
                    return mc.player.posY + 8.5D;
                }
                return mc.player.posY + 9.5D;
            } else if (mc.getCurrentServerData().serverIP.toLowerCase().contains("9b9t")) {
                BlockPos currentPos = new BlockPos(mc.player.posX, mc.player.posY + 9D, mc.player.posZ);
                if (mc.world.getBlockState(currentPos).getBlock() instanceof BlockAir && mc.world.getBlockState(currentPos.up()).getBlock() instanceof BlockAir) {
                    return mc.player.posY + 9D;
                } else {
                    for (int i = 10; i < 20; i++) {
                        BlockPos iPos = new BlockPos(mc.player.posX, mc.player.posY + i, mc.player.posZ);
                        if (mc.world.getBlockState(iPos).getBlock() instanceof BlockAir && mc.world.getBlockState(iPos.up()).getBlock() instanceof BlockAir) {
                            return mc.player.posY + i;
                        }
                    }
                }
                return mc.player.posY + 20D;
            }
        }
        BlockPos currentPos = new BlockPos(mc.player.posX, mc.player.posY - 9D, mc.player.posZ);
        if (mc.world.getBlockState(currentPos).getBlock() instanceof BlockAir && mc.world.getBlockState(currentPos.up()).getBlock() instanceof BlockAir) {
            return mc.player.posY - 9D;
        } else {
            for (int i = -10; i > -20; i--) {
                BlockPos iPos = new BlockPos(mc.player.posX, mc.player.posY - i, mc.player.posZ);
                if (mc.world.getBlockState(iPos).getBlock() instanceof BlockAir && mc.world.getBlockState(iPos.up()).getBlock() instanceof BlockAir) {
                    return mc.player.posY - i;
                }
            }
        }
        return mc.player.posY - 24D;
    }


    @Subscribe
    public Listener<PacketEvent.Receive> onSPacketPlayerPosLook = new Listener<>(event -> {
        if (mc.currentScreen instanceof GuiDownloadTerrain) {
            toggle();
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook && !strict.getValue()) {
            ((ISPacketPlayerPosLook) event.getPacket()).setYaw(mc.player.rotationYaw);
            ((ISPacketPlayerPosLook) event.getPacket()).setPitch(mc.player.rotationPitch);
        }
    });

    @Subscribe
    public Listener<PlayerPushOutOfBlocksEvent> onPush = new Listener<>(e -> {
        e.setCancelled(true);
    });

    private int getHeadSlot(){
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemSkull) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private int getBlockSlot(){
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (((block instanceof BlockObsidian && !onlyEChest.getValue()) || block instanceof BlockEnderChest || block instanceof BlockChest)) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }


    @Override
    public void onEnable() {
        super.onEnable();
        mc.player.swingArm(EnumHand.MAIN_HAND);
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }
        if (!mc.player.onGround) {
            toggle();
            return;
        }
        state = State.WAITING;
    }
}
