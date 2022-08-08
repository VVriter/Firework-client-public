package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.TickTimer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleManifest(name = "PistonAura", category = Module.Category.COMBAT)
public class PistonAura extends Module {

    public Setting<Integer> targetRange = new Setting<>("TargetRange", 6, this, 1, 6);
    public Setting<Integer> placeRange = new Setting<>("PlaceRange", 6, this, 1, 6);

    public Setting<Boolean> blocks = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockPlacer.switchModes> blockSwitch = new Setting<>("PlaceSwitch", BlockPlacer.switchModes.Fast, this).setVisibility(v-> blocks.getValue());
    public Setting<Boolean> blockRotate = new Setting<>("Rotate", true, this).setVisibility(v-> blocks.getValue());
    public Setting<Boolean> blockPacket = new Setting<>("Packet", true, this).setVisibility(v-> blocks.getValue());

    public Setting<Boolean> delays = new Setting<>("Delays", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> placeBlocksDelay = new Setting<>("PlaceBlockDelay", 10, this, 1, 20).setVisibility(v-> delays.getValue());
    public Setting<Integer> placeCrystalDelay = new Setting<>("PlaceCrystalDelay", 10, this, 1, 20).setVisibility(v-> delays.getValue());
    public Setting<Integer> breakCrystalDelay = new Setting<>("BreakCrystalDelay", 10, this, 1, 20).setVisibility(v-> delays.getValue());

    BlockPlacer blockPlacer;

    TickTimer timer;

    int stage;

    public BlockPos test;

    List<BlockPos> toRender = new ArrayList<>();

    public List<Vec3i> redStoneTargetBlocks = Arrays.asList(
            new Vec3i(0, 1, 1),
            new Vec3i(0, 1, 0),
            new Vec3i(0, 1, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1),
            new Vec3i(0, -1, 1),
            new Vec3i(0, -1, 0),
            new Vec3i(0, -1, -1),
            new Vec3i(1, 1, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(1, -1, 0),
            new Vec3i(-1, 1, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(-1, -1, 0)
    );

    public List<Vec3i> pistonInteractionBlocks = Arrays.asList(
            new Vec3i(0, 1, 0),
            new Vec3i(0, -1, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(1, 0, 0),
            new Vec3i(0, 0, -1),
            new Vec3i(-1, 0, 0)
    );

    public List<Pair<Block, Action>> pistons = Arrays.asList(
            new Pair<>(Blocks.PISTON, Action.PLACE),
            new Pair<>(Blocks.STICKY_PISTON, Action.PLACE)
    );

    public List<Pair<Block, Action>> redStone = Arrays.asList(
            new Pair<>(Blocks.REDSTONE_BLOCK, Action.PLACE),
            new Pair<>(Blocks.REDSTONE_TORCH, Action.PLACE),
            new Pair<>(Blocks.LEVER, Action.PLACE_USE)
    );

    public enum Action{
        PLACE, PLACE_USE
    }

    public Pair<Block, Action> getPiston(){
        Pair<Block, Action> result = null;
        for(Pair<Block, Action> block : pistons)
            if(InventoryUtil.getHotbarItemSlot(Item.getItemFromBlock(block.one)) != -1)
                result = block;
        return result;
    }

    public Pair<Block, Action> getRedStone(){
        Pair<Block, Action> result = null;
        for(Pair<Block, Action> block : redStone)
            if(InventoryUtil.getHotbarItemSlot(Item.getItemFromBlock(block.one)) != -1)
                result = block;
        return result;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        blockPlacer = new BlockPlacer(this, blockSwitch, blockRotate, blockPacket);

        timer = new TickTimer();
        stage = 1;
        toRender.clear();
        test = EntityUtil.getFlooredPos(mc.player).add(0, 20, 0);
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(render3dE -> {
       if(fullNullCheck() || test == null) return;
        for(Vec3i vec3i : redStoneTargetBlocks){
            RenderUtils.drawBoxESP(test.add(vec3i), Color.green, 3, true, true, 100, 1);
        }
        for(BlockPos pos : toRender){
            RenderUtils.drawBoxESP(pos, Color.green, 4, true, true, 100, 1);
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if(!mc.player.inventory.hasItemStack(new ItemStack(Items.END_CRYSTAL))
                || !(mc.player.inventory.hasItemStack(new ItemStack(Blocks.PISTON))
                || mc.player.inventory.hasItemStack(new ItemStack(Blocks.STICKY_PISTON)))
                || !(mc.player.inventory.hasItemStack(new ItemStack(Blocks.REDSTONE_TORCH))
                || mc.player.inventory.hasItemStack(new ItemStack(Blocks.REDSTONE_BLOCK)))){
            MessageUtil.sendError("No Pistons/Crystals/RedStone found in the hotbar", -1117);
            onDisable();
            return;
        }

        EntityPlayer target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;

        switch (stage){
            case 1:
                Pair<BlockPos, EnumFacing> pistonPlacePos = getPistonPlacePos(target);
                if(pistonPlacePos.one != null && pistonPlacePos.two != null) {
                    if (timer.hasPassedTicks(placeBlocksDelay.getValue())) {

                        blockPlacer.placeBlockEnumFacing(pistonPlacePos.one, pistonPlacePos.two, getPiston().one);

                        stage = 3;
                        timer.reset();
                    }
                }else
                    stage = 3;
                break;
            case 3:
                BlockPos redstoneToPlace = getRedStoneToPlace(target);
                //toRender = getPistonInteractionBlocks(getPistonPlacedPos(target));
                BlockPos pos = getPistonPlacedPos(target);
                System.out.println(pos);
                if(pos != null){
                    toRender = getPistonInteractionBlocks(getPistonPlacedPos(target));
                    stage = 4;
                }
                if(redstoneToPlace != null){
                    if(timer.hasPassedTicks(placeBlocksDelay.getValue())){
                    }
                }else
                    //stage = 4;
                break;
        }
    });

    public List<BlockPos> getRedStoneTargetBlocks(BlockPos redStone){
        ArrayList<BlockPos> list = new ArrayList<>();
        for(Vec3i vec3i : redStoneTargetBlocks)
            list.add(redStone.add(vec3i));

        return list;
    }

    public List<BlockPos> getPistonInteractionBlocks(BlockPos piston){
        ArrayList<BlockPos> list = new ArrayList<>();
        for(Vec3i vec3i : pistonInteractionBlocks) {
            //Gets block pos
            BlockPos pos = piston.add(vec3i);
            //Checks if can place red stone thing
            if(!(canPlaceBlock(pos, getRedStone().one) == 0) || piston.offset(mc.world.getBlockState(piston).getValue(BlockDirectional.FACING)).equals(pos)) continue;
            list.add(pos);
        }

        return list;
    }

    public Pair<BlockPos, EnumFacing> getPistonPlacePos(EntityPlayer target){
        double lowestDistance = Integer.MAX_VALUE;
        final BlockPos targetPos = EntityUtil.getFlooredPos(target);
        final BlockPos playerPos = EntityUtil.getFlooredPos(mc.player);
        BlockPos placePos = null;
        EnumFacing face = null;

        for (EnumFacing facing : EnumFacing.values()) {
            if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
            BlockPos predictedPlacePos = targetPos.offset(EnumFacing.UP).offset(facing).offset(facing);
            if (BlockUtil.distance(playerPos, predictedPlacePos) < lowestDistance) {
                if (canPlaceBlock(predictedPlacePos) == 0) {
                    lowestDistance = BlockUtil.distance(playerPos, predictedPlacePos);
                    placePos = predictedPlacePos;
                    face = facing;
                }
            }
        }
        return new Pair<>(placePos, face);
    }

    public BlockPos getPistonPlacedPos(EntityPlayer target){
        final BlockPos targetPos = EntityUtil.getFlooredPos(target);
        BlockPos placePos = null;

        for(BlockPos pos : BlockUtil.getSphere(placeRange.getValue(), true)){
            //Checks if block instanceof piston base block class
            if(!(BlockUtil.getBlock(pos) instanceof BlockPistonBase)) continue;
            //Gets facing value
            EnumFacing face = mc.world.getBlockState(pos).getValue(BlockDirectional.FACING);
            //Checks if piston is looking at the target
            if(pos.offset(face).offset(face).offset(EnumFacing.DOWN).equals(targetPos)){
                //Checks if it is possible to power it
                if(true){
                    placePos = pos;
                    break;
                }
            }
        }
        return placePos;
    }

    public BlockPos getRedStoneToPlace(EntityPlayer target){
        return null;
    }

    public boolean canBePowered(BlockPos piston){
        return !getPistonInteractionBlocks(piston).isEmpty();
    }

    public int canPlaceBlock(BlockPos pos){
        //Checks if block at the pos is filled
        if(!BlockUtil.isAir(pos))
            return -1;

        //Has touching blocks check
        boolean canPlace = false;
        for(BlockPos blockPos : BlockUtil.getTouchingBlocks(pos)){
            if(!BlockUtil.isAir(blockPos)){
                canPlace = true;
                break;
            }
        }

        if(!canPlace)
            return -2;

        //Checks if entities block placing
        ArrayList<Entity> entities = new ArrayList<Entity>();
        entities.addAll(mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)));
        for (Entity entity : entities) {
            if (entity.isEntityAlive()) {
                return -3;
            }
        }
        return 0;
    }

    public int canPlaceBlock(BlockPos pos, Block block){
        //Checks if block at the pos is filled
        if(!BlockUtil.isAir(pos))
            return -1;

        //Has touching blocks check
        boolean canPlace = false;
        for(BlockPos blockPos : BlockUtil.getTouchingBlocks(pos)){
            if(!BlockUtil.isAir(blockPos) && ((block != Blocks.REDSTONE_TORCH) || !(BlockUtil.getBlock(blockPos) instanceof BlockPistonBase))){
                canPlace = true;
                break;
            }
        }

        if(!canPlace)
            return -2;

        //Checks if entities block placing
        ArrayList<Entity> entities = new ArrayList<Entity>();
        entities.addAll(mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)));
        for (Entity entity : entities) {
            if (entity.isEntityAlive()) {
                return -3;
            }
        }
        return 0;
    }
}
