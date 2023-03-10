package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.Movement.Step;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.MotionUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

import static com.firework.client.Implementations.Utill.InventoryUtil.getHotbarItemSlot;

@ModuleManifest(
        name = "Surround",
        category = Module.Category.COMBAT,
        description = "Prevents AutoCrystal from your opponents"
)
public class Surround extends Module {
    private Setting<jumpMode> jump = new Setting<>("Jump", jumpMode.Continue, this);
    private enum jumpMode{
        Continue, Disable, None
    }


    private Setting<template> templateMode = new Setting<>("Template", template.Classic, this);
    private enum template{
        Classic, NoFacePlace
    }

    private Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);
    private Setting<MotionUtil.centerModes> centerMode = new Setting<>("CMode", MotionUtil.centerModes.Motion, this).setVisibility(()-> shouldCenter.getValue(true));

    private Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", false, this);
    private Setting<Integer> placeDelay = new Setting<>("PlaceDelayMs", 0, this, 0, 100);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this);

    private Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    private Setting<Boolean> packet = new Setting<>("Packet", true, this);

    private Setting<Boolean> deathDisable = new Setting<>("DeathDisable", false, this);

    private ArrayList<BlockPos> line;

    private BlockPlacer blockPlacer;

    private Timer placeTimer;

    private boolean first;

    public static long time = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) super.onDisableLog();

        if(!containsAir(blocksToPlace()) && shouldToggle.getValue())
            onDisable();

        placeTimer = new Timer();
        placeTimer.reset();

        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);

        line = new ArrayList<>();

        first = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        line = null;
        blockPlacer = null;
        placeTimer = null;
    }

    @Subscribe(priority = Listener.Priority.HIGHEST)
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange packet2 = ((SPacketBlockChange) event.getPacket());
            if(packet2.blockState.getBlock() == Blocks.AIR){
                if(Arrays.asList(blocksToPlace()).contains(packet2.getBlockPosition())){
                    if(InventoryUtil.getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) != -1){
                        blockPlacer.placeBlock(packet2.getBlockPosition(), Blocks.OBSIDIAN);
                        System.out.println(1);
                    }
                }
            }
        }
    });

    @Subscribe(priority = Listener.Priority.HIGHEST)
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (mc.player == null || mc.world == null) return;

        if(jump.getValue(jumpMode.Disable) && (mc.gameSettings.keyBindJump.isKeyDown())) {
            onDisableLog();
            return;
        }else if(jump.getValue(jumpMode.Continue) && mc.gameSettings.keyBindJump.isKeyDown() && isAir(EntityUtil.getFlooredPos(mc.player).add(0, -1, 0)) && BlockUtil.isValid(EntityUtil.getFlooredPos(mc.player).add(0, -1, 0))){
            blockPlacer.placeBlock(EntityUtil.getFlooredPos(mc.player).add(0, -1, 0), Blocks.OBSIDIAN);
        }

        if (first) {
            if (shouldCenter.getValue())
                MotionUtil.autoCenter(centerMode);

            if (shouldToggle.getValue()) {
                doSurround();
                onDisableLog();
                return;
            }
            first = false;
        }

        //Stops process if didn't find obby in a hotbar
        if (getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            onDisable();
            return;
        }

        final BlockPos[] blockToPlace = blocksToPlace();

        if (containsAir(blockToPlace))
            doSurround(blockToPlace);
    });

    private void doSurround(BlockPos... blockToPlace){
        if(line == null || blockToPlace == null) return;
        for (BlockPos pos : blockToPlace)
            if (isAir(pos) && !line.contains(pos))
                line.add(pos);

        ArrayList<BlockPos> placedBlocks = new ArrayList<>();

        for (BlockPos pos : line) {
            if (placeTimer.hasPassedMs(placeDelay.getValue())) {
                if (BlockUtil.getPossibleSides(pos).isEmpty() && isValid(pos.add(0, -1, 0)))
                    blockPlacer.placeBlock(pos.add(0, -1, 0), Blocks.OBSIDIAN);
                else {
                    if(!isValid(pos)) return;
                    blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
                    placedBlocks.add(pos);
                }
                placeTimer.reset();
            } else {
                break;
            }
        }

        line.removeAll(placedBlocks);
    }
    //Returns blocks to place
    public BlockPos[] blocksToPlace() {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        if(templateMode.getValue(template.Classic))
            return new BlockPos[]{p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
        else if(templateMode.getValue(template.NoFacePlace))
            return new BlockPos[]{p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1),
                    p.add(1, 1, 0), p.add(-1, 1, 0), p.add(0, 1, 1), p.add(0, 1, -1)};

        return null;
    }

    //Checks if a block pos array contains any air blocks
    public boolean containsAir(BlockPos... blocks){
        for(BlockPos blockPos : blocks){
            if(BlockUtil.getBlock(blockPos) == Blocks.AIR){
                return true;
            }
        }
        return false;
    }

    //Returns true if there is no block at a give pos
    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }

    //Checks if block is valid
    public static boolean isValid(BlockPos pos){
        if (!mc.world.checkNoEntityCollision(new AxisAlignedBB(pos)))
            return false;
        return true;
    }
}
