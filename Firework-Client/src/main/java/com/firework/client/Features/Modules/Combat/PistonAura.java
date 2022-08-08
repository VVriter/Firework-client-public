package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.TickTimer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@ModuleManifest(name = "PistonAura", category = Module.Category.COMBAT)
public class PistonAura extends Module {

    public Setting<Integer> targetRange = new Setting<>("TargetRange", 10, this, 1, 6);

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
            if(InventoryUtil.hasItem(Item.getItemFromBlock(block.one)))
                result = block;
        return result;
    }

    public Pair<Block, Action> getRedStone(){
        Pair<Block, Action> result = null;
        for(Pair<Block, Action> block : redStone)
            if(InventoryUtil.hasItem(Item.getItemFromBlock(block.one)))
                result = block;
        return result;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        blockPlacer = new BlockPlacer(this, blockSwitch, blockRotate, blockPacket);

        timer = new TickTimer();
        stage = 1;
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(render3dE -> {
       if(fullNullCheck()) return;
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
                Pair<BlockPos, EnumFacing> pistonPlacePos = pistonPlacePos(target);
                if(pistonPlacePos.one != null && pistonPlacePos.two != null) {
                    if (timer.hasPassedTicks(placeBlocksDelay.getValue())) {

                        blockPlacer.placeBlockEnumFacing(pistonPlacePos.one, pistonPlacePos.two, getPiston().one);

                        stage = 2;
                        timer.reset();
                    }
                }else
                    stage = 2;
                break;
        }
    });

    public Pair<BlockPos, EnumFacing> pistonPlacePos(EntityPlayer target){
        double lowestDistance = Integer.MAX_VALUE;
        final BlockPos targetPos = EntityUtil.getFlooredPos(target);
        final BlockPos playerPos = EntityUtil.getFlooredPos(mc.player);
        BlockPos placePos = null;
        EnumFacing face = null;

        for (EnumFacing facing : EnumFacing.values()) {
            if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
            BlockPos predictedPlacePos = targetPos.offset(EnumFacing.UP).offset(facing).offset(facing);
            if (BlockUtil.distance(playerPos, predictedPlacePos) < lowestDistance) {
                if (BlockUtil.canPlaceBlock(predictedPlacePos)) {
                    lowestDistance = BlockUtil.distance(playerPos, predictedPlacePos);
                    placePos = predictedPlacePos;
                    face = facing;
                }
            }
        }
        return new Pair<>(placePos, face);
    }
}
