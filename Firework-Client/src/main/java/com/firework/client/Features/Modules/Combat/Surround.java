package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.MotionUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.InventoryUtil.getHotbarItemSlot;

@ModuleManifest(name = "Surround", category = Module.Category.COMBAT)
public class Surround extends Module {
    private Setting<Boolean> shouldDisableOnJump = new Setting<>("DisableOnJump", true, this);

    private Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);
    private Setting<MotionUtil.centerModes> centerMode = new Setting<>("CMode", MotionUtil.centerModes.Motion, this, MotionUtil.centerModes.values()).setVisibility(v-> shouldCenter.getValue(true));

    private Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", false, this);
    private Setting<Integer> placeDelay = new Setting<>("PlaceDelayMs", 0, this, 0, 100);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this, BlockPlacer.switchModes.values());

    private Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    private Setting<Boolean> packet = new Setting<>("Packet", true, this);

    private ArrayList<BlockPos> line;

    private BlockPlacer blockPlacer;

    private Timer placeTimer;

    private boolean first;

    @Override
    public void onEnable() {
        super.onEnable();
        if(!containsAir(blockToPlace()))
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

    @Override
    public void onTick() {
        super.onTick();
        if(mc.player == null || mc.world == null) return;

        if(first) {
            if (shouldCenter.getValue())
                MotionUtil.autoCenter(centerMode);

            if (shouldToggle.getValue()) {
                doSurround();
                onDisable();
            }
            first = false;
        }

        //Stops process if web wasn't found in a hotbar
        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            return;
        }

        final BlockPos[] blockToPlace = blockToPlace();

        if(containsAir(blockToPlace))
            doSurround(blockToPlace);
    }

    private void doSurround(BlockPos... blockToPlace){
        for(BlockPos pos : blockToPlace)
            if(isAir(pos) && !line.contains(pos))
                line.add(pos);

        ArrayList<BlockPos> placedBlocks = new ArrayList<>();

        for(BlockPos pos : line){
            if(placeTimer.hasPassedMs(placeDelay.getValue())){
                if(BlockUtil.getPossibleSides(pos).isEmpty())
                    blockPlacer.placeBlock(pos.add(0, -1, 0), Blocks.OBSIDIAN);
                else {
                    blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
                    placedBlocks.add(pos);
                }
                placeTimer.reset();
            }else {
                break;
            }
        }

        line.removeAll(placedBlocks);
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            if(shouldDisableOnJump.getValue())
                onDisable();
        }
    }

    //Returns blocks to place
    public BlockPos[] blockToPlace() {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        return new BlockPos[]{p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
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
}
