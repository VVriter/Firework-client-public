package com.firework.client.Features.Modules.Combat;


import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.InventoryUtil.getHotbarItemSlot;

@ModuleManifest(name = "Trap", category = Module.Category.COMBAT)
public class Trap extends Module {

    private Setting<Integer> targetDistance = new Setting<>("TargetDistance", 3, this, 0, 5);

    private Setting<Integer> placedDelayMs = new Setting<>("PlaceDelayMs", 0, this, 0, 100);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Fast, this);

    private Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    private Setting<Boolean> packet = new Setting<>("Packet", false, this);

    private Setting<Boolean> autoDisable = new Setting<>("AutoDisable", true, this);

    private Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 1,1), this);

    private ArrayList<BlockPos> line = new ArrayList<>();

    private Timer placeTimer;

    private BlockPlacer blockPlacer;

    private BlockPos lastTargetPos = null;

    @Override
    public void onEnable() {
        super.onEnable();
        placeTimer = new Timer();
        placeTimer.reset();

        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        placeTimer = null;
        line.clear();

        blockPlacer = null;
    }

    @Override
    public void onTick() {
        super.onTick();
        EntityPlayer entityTarget = PlayerUtil.getClosestTarget(targetDistance.getValue());
        if(entityTarget==null) {
            if(!line.isEmpty())
                line.clear();

            if(autoDisable.getValue())
                onDisable();

            return;
        }

        if(autoDisable.getValue() && isTrapped(entityTarget))
            onDisable();

        //Stops process if web wasn't found in a hotbar
        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            return;
        }

        boolean shouldAdd = false;
        if(lastTargetPos == null) {
            lastTargetPos = EntityUtil.getFlooredPos(entityTarget);
            shouldAdd = true;
        }else{
            if(EntityUtil.getFlooredPos(entityTarget) != lastTargetPos)
                shouldAdd = true;
        }

        if(shouldAdd) {
            if(!isTrapped(entityTarget)) {
                for (BlockPos pos : trapBlocks(entityTarget)) {
                    if (isAir(pos) && !line.contains(pos))
                        line.add(pos);
                }
            }
        }

        ArrayList<BlockPos> blocksToClear = new ArrayList<>();

        for(BlockPos pos : line){
            if(placeTimer.hasPassedMs(placedDelayMs.getValue())){
                if(BlockUtil.canPlaceBlock(pos)) {
                    blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
                    blocksToClear.add(pos);
                    placeTimer.reset();
                }
            }else{
                break;
            }
        }

        line.removeAll(blocksToClear);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        for(BlockPos pos : line){
            new BlockRenderBuilder(pos)
                    .addRenderModes(
                            new RenderMode(RenderMode.renderModes.OutLine, color.getValue().toRGB(), 3f)
                    ).render();
        }
    }
    //Gets fist layer of blocks to place
    private BlockPos[] trapBlocks(EntityPlayer target) {
        BlockPos p = EntityUtil.getFlooredPos(target);
        return new BlockPos[]{p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1),
                p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1),
                p.add(1, 1, 0), p.add(-1, 1, 0), p.add(0, 1, 1), p.add(0, 1, -1),
                nearestTrapBlock(p.add(1, 2, 0), p.add(-1, 2, 0), p.add(0, 2, 1), p.add(0, 2, -1)),
                p.add(0, 2, 0)};
    }

    //Return nearest block from a given list
    private BlockPos nearestTrapBlock(BlockPos... blocks){
        double lastDistance = -1;
        BlockPos lastBlock = null;
        for(BlockPos blockPos : blocks){
            if(lastDistance == -1){
                lastDistance = BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player));
                lastBlock = blockPos;
            }else{
                if(lastDistance>BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player))) {
                    lastBlock = blockPos;
                    lastDistance = BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player));
                }
            }
        }
        return lastBlock;
    }

    //Returns true if given player is trapped
    private boolean isTrapped(EntityPlayer target){
        for(BlockPos blockPos : trapBlocks(target))
            if(isAir(blockPos))
                return false;

        return true;
    }

    //Reuturn true if block at given blockpos is empty
    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }
}