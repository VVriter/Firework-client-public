package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.InventoryUtil.getHotbarItemSlot;

@ModuleManifest(name = "TrapRewrite", category = Module.Category.COMBAT)
public class TrapRewrite extends Module {

    private Setting<templates> templateMode = new Setting<>("Template", templates.Full, this);
    private enum templates{
        Full, FacePlace, FeetCrystal
    }

    private Setting<Integer> targetDistance = new Setting<>("TargetDistance", 3, this, 0, 5);

    private Setting<Integer> placedDelay = new Setting<>("PlaceDelay", 1, this, 0, 20);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this);

    private Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    private Setting<Boolean> packet = new Setting<>("Packet", false, this);

    private Setting<Boolean> autoDisable = new Setting<>("AutoDisable", true, this);

    private Setting<HSLColor> color = new Setting<>("Color", new HSLColor(150, 50,50), this);

    private ArrayList<BlockPos> line = new ArrayList<>();

    private int remaingDelay;

    private BlockPlacer blockPlacer;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) return;
        remaingDelay = 0;

        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        line.clear();

        blockPlacer = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        EntityPlayer entityTarget = PlayerUtil.getClosestTarget(targetDistance.getValue());
        if(entityTarget == null) {
            if(!line.isEmpty())
                line.clear();

            if(autoDisable.getValue())
                onDisableLog();
            return;
        }

        if(autoDisable.getValue() && !canContinueTrapping(entityTarget)) {
            onDisableLog();
            return;
        }

        if(canContinueTrapping(entityTarget)){
            for(BlockPos pos : trapBlocks(entityTarget)){
                if(line.contains(pos) || !BlockUtil.isAir(pos) || !BlockUtil.isValid(pos)) continue;
                line.add(pos);
            }
        }

        if(remaingDelay > 0)
            remaingDelay--;

        if(remaingDelay != 0) return;
        remaingDelay = placedDelay.getValue();

        //Stops process if web wasn't found in a hotbar
        if(getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            onDisable();
            return;
        }

        BlockPos posToRemove = null;
        for(BlockPos pos : line){
            posToRemove = pos;
            if(BlockUtil.isAir(pos) && BlockUtil.isValid(pos))
                blockPlacer.placeTrapBlock(pos, Blocks.OBSIDIAN);
            break;
        }
        line.remove(posToRemove);
    });

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(worldRender3DEvent -> {
        for(BlockPos pos : line){
            RenderUtils.drawBoxESP(pos, color.getValue().toRGB(), 4, true, true, 100, 1);
        }
    });
    //Gets fist layer of blocks to place
    private BlockPos[] trapBlocks(EntityPlayer target) {
        BlockPos p = EntityUtil.getFlooredPos(target);
        if(templateMode.getValue(templates.Full)) {
            return new BlockPos[]{
                    //First surround layer -1
                    p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1),
                    //Second surround layer (Feet)
                    p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1),
                    //Third surround layer (Face)
                    p.add(1, 1, 0), p.add(-1, 1, 0), p.add(0, 1, 1), p.add(0, 1, -1),
                    //Nearest helping block to trap upside
                    farthestTrapBlock(p.add(1, 2, 0), p.add(-1, 2, 0), p.add(0, 2, 1), p.add(0, 2, -1)),
                    //Upside trap block
                    p.add(0, 2, 0)
            };
        }else if(templateMode.getValue(templates.FacePlace)){
            BlockPos farthestHelpBlock = farthestTrapBlock(
                    p.add(1, 1, 0), p.add(-1, 1, 0), p.add(0, 1, 1), p.add(0, 1, -1)
            );
            return new BlockPos[]{
                    //First surround layer -1
                    p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1),
                    //Second surround layer (Feet)
                    p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1),
                    //Farthest helping blocks
                    farthestHelpBlock, farthestHelpBlock.add(0, 1, 0),
                    //Upside trap block
                    p.add(0, 2, 0)
            };
        }else if(templateMode.getValue(templates.FeetCrystal)){
            BlockPos farthestHelpBlock = farthestTrapBlock(
                    p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)
            );
            BlockPos nearestCrystalPlacePos = nearestCrystalPlaceValidBlock(
                    p.add(2, -1, 0), p.add(-2, -1, 0), p.add(0, -1, 2), p.add(0, -1, -2),
                    p.add(1, -1, 1), p.add(-1, -1, 1), p.add(-1, 0, 1), p.add(1, -1, -1)
            );
            return new BlockPos[]{
                    //First surround layer -1
                    p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1),
                    //Farthest helping blocks
                    farthestHelpBlock, farthestHelpBlock.add(0, 1, 0), farthestHelpBlock.add(0, 2, 0),
                    //Upside trap block
                    p.add(0, 2, 0),
                    //Upside offsets
                    p.add(1, 2, 0), p.add(-1, 2, 0), p.add(0, 2, 1), p.add(0, 2, -1),
                    //Second surround layer (Face)
                    p.add(1, 1, 0), p.add(-1, 1, 0), p.add(0, 1, 1), p.add(0, 1, -1),

                    //Nearest crystal place block
                    nearestCrystalPlacePos
            };
        }

        return null;
    }

    //Returns the nearest block from a given list
    private BlockPos nearestTrapBlock(BlockPos... blocks){
        double lastDistance = -1;
        BlockPos lastBlock = null;
        for(BlockPos blockPos : blocks){
            if(blockPos == null) continue;
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

    //Returns the farthest block from a given list
    private BlockPos farthestTrapBlock(BlockPos... blocks){
        double lastDistance = -1;
        BlockPos lastBlock = null;
        for(BlockPos blockPos : blocks){
            if(blockPos == null) continue;
            if(lastDistance == -1){
                lastDistance = BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player));
                lastBlock = blockPos;
            }else{
                if(lastDistance<BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player))) {
                    lastBlock = blockPos;
                    lastDistance = BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player));
                }
            }
        }
        return lastBlock;
    }

    //Returns the nearest block from a given list where u can place a crystal
    private BlockPos nearestCrystalPlaceValidBlock(BlockPos... blocks){
        double lastDistance = -1;
        BlockPos lastBlock = null;
        for(BlockPos blockPos : blocks){
            if(blockPos == null) continue;
            if(lastDistance == -1){
                if(BlockUtil.isValid(blockPos.add(0, 1, 0))
                        && BlockUtil.isValid(blockPos.add(0, 2, 0))) {
                    lastDistance = BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player));
                    lastBlock = blockPos;
                }
            }else{
                if(lastDistance>BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player))
                        && BlockUtil.isValid(blockPos.add(0, 1, 0))
                        && BlockUtil.isValid(blockPos.add(0, 2, 0))) {
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
            if(BlockUtil.isAir(blockPos))
                return false;

        return true;
    }

    //Returns true if given player is trapped
    private boolean canContinueTrapping(EntityPlayer target){
        for(BlockPos blockPos : trapBlocks(target))
            if(BlockUtil.isAir(blockPos) && !BlockUtil.isValid(blockPos))
                return false;

        return true;
    }
}
