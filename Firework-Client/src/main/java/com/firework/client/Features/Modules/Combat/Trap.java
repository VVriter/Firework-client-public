package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ModuleManifest(name = "Trap", category = Module.Category.COMBAT)
public class Trap extends Module {

    private Setting<templates> templateMode = new Setting<>("Template", templates.Full, this);

    private enum templates{
        Full, FacePlace, FeetCrystal
    }

    private Setting<Double> targetRange = new Setting<>("TargetRange", 4d, this, 1, 6);

    private Setting<Integer> placeDelay = new Setting<>("PlaceDelay", 1, this, 1, 20);
    private Setting<Integer> placeRange = new Setting<>("PlaceRange", 1, this, 1, 6);


    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this);
    private Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    private Setting<Boolean> packet = new Setting<>("Packet", false, this);

    private Setting<Boolean> multiTrap = new Setting<>("MultiTrap", false, this);

    private Setting<Boolean> autoDisable = new Setting<>("AutoDisable", false, this);

    private Setting<HSLColor> color = new Setting<>("Color", new HSLColor(260, 50, 50), this);

    private LinkedList<BlockPos> queue = new LinkedList<>();

    BlockPlacer blockPlacer;

    int remainingDelay;

    LinkedList<EntityPlayer> currentTargets;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) {
            onDisable();
            return;
        }
        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);

        remainingDelay = placeDelay.getValue();

        currentTargets = new LinkedList<>();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        currentTargets = null;

        blockPlacer = null;
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(worldRender3DEvent -> {
        for(BlockPos pos : queue){
            RenderUtils.drawBoxESP(pos, color.getValue().toRGB(), 4, true, true, 100, 1);
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if(InventoryUtil.getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1){
            MessageUtil.sendError("No obby found in the hotbar | Disabling", -1117);
            onDisable();
            return;
        }

        remainingDelay--;
        if(remainingDelay > 0) return;
        remainingDelay  = placeDelay.getValue();

        //Targeting block
        LinkedList<EntityPlayer> targets = PlayerUtil.getClosestTargets(targetRange.getValue());

        EntityPlayer target = targets.peek();

        //Filters targets that we can trap if multi trapping is enabled
        if(multiTrap.getValue() && currentTargets.peek() != null && !canContinueTrapping(currentTargets.peek()))
            target = currentTargets.stream().filter(player -> canContinueTrapping(player))
                .collect(Collectors.toCollection(LinkedList<EntityPlayer>::new)).peek();

        if(target == null){
            if(!queue.isEmpty())
                queue.clear();

            if(autoDisable.getValue())
                onDisableLog();
            return;
        }

        if(!multiTrap.getValue() && autoDisable.getValue() && !canContinueTrapping(target)) {
            onDisableLog();
            return;
        }

        //Logic/Place block
        for(BlockPos pos : trapBlocks(target)){
            if(isValid(pos) && !queue.contains(pos))
                queue.add(pos);
        }

        if(isValid(queue.peek()))
            blockPlacer.placeTrapBlock(queue.peek(), Blocks.OBSIDIAN);
        queue.remove(queue.peek());
    });

    //Gets list of blocks to place
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
            if(BlockUtil.isAir(blockPos) && BlockUtil.isValid(blockPos) && !BlockUtil.getPossibleSides(blockPos).isEmpty())
                return true;

        return false;
    }

    //Returns true if given blockPos is trap valid
    public boolean isValid(BlockPos pos){
        return BlockUtil.isAir(pos) && BlockUtil.isValid(pos)
                && !BlockUtil.getPossibleSides(pos).isEmpty()
                && mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0)
                .distanceTo(new Vec3d(pos).add(0.5, -0.5, 0.5)) <= placeRange.getValue();
    }
}
