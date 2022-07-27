package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Events.WorldRender3DEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "CevBreaker", category = Module.Category.COMBAT)
public class CevBreaker extends Module {


    public Setting<Integer> targetRange = new Setting<>("TargetRange", 3, this, 1, 6);

    public Setting<Boolean> items = new Setting<>("Items", false, this).setMode(Setting.Mode.SUB);
    public Setting<ItemUser.switchModes> itemSwitch = new Setting<>("SwitchMode", ItemUser.switchModes.Fast, this).setVisibility(v-> items.getValue());
    public Setting<Boolean> itemRotate = new Setting<>("Rotate", true, this).setVisibility(v-> items.getValue());

    public Setting<Boolean> blocks = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockPlacer.switchModes> blockSwitch = new Setting<>("PlaceSwitch", BlockPlacer.switchModes.Fast, this).setVisibility(v-> blocks.getValue());
    public Setting<BlockBreaker.mineModes> breakMode = new Setting<>("BreakMode", BlockBreaker.mineModes.Classic, this).setVisibility(v-> blocks.getValue());
    public Setting<Boolean> blockRayTrace = new Setting<>("RayTrace", true, this).setVisibility(v-> blocks.getValue());
    public Setting<Boolean> blockRotate = new Setting<>("Rotate", true, this).setVisibility(v-> blocks.getValue());
    public Setting<Boolean> blockPacket = new Setting<>("Packet", true, this).setVisibility(v-> blocks.getValue());

    public Setting<Boolean> delays = new Setting<>("Delays", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> placeBlockDelay = new Setting<>("PlaceBlockDelayMs", 80, this, 1, 200).setVisibility(v-> delays.getValue());
    public Setting<Integer> placeCrystalDelay = new Setting<>("PlaceCrystalDelayMs", 80, this, 1, 200).setVisibility(v-> delays.getValue());
    public Setting<Integer> breakBlockDelay = new Setting<>("BreakBlockDelayMs", 80, this, 1, 200).setVisibility(v-> delays.getValue());
    public Setting<Integer> breakCrystalDelay = new Setting<>("BreakCrystalDelayMs", 80, this, 1, 200).setVisibility(v-> delays.getValue());

    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 50, 50), this);

    EntityPlayer target;
    BlockPos upside;

    ItemUser itemUser;
    BlockPlacer blockPlacer;
    BlockBreaker blockBreaker;
    public Timer timer;
    public int stage;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) onDisable();

        itemUser = new ItemUser(this, itemSwitch, itemRotate);
        blockPlacer = new BlockPlacer(this, blockSwitch, blockRotate, blockPacket);
        blockBreaker = new BlockBreaker(this, breakMode, blockRotate, blockRayTrace, blockPacket);
        timer = new Timer();

        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        stage = getStage();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        itemUser = null;
        blockPlacer = null;
        blockBreaker = null;
        timer = null;
    }

    @Subscribe
    public Listener<WorldRender3DEvent> onRender = new Listener<>(worldRender3DEvent -> {
       if(upside == null) return;
       new BlockRenderBuilder(upside)
               .addRenderModes(
                       new RenderMode(RenderMode.renderModes.Fill, color.getValue().toRGB())
               ).render();
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;

        if(!mc.player.inventory.hasItemStack(new ItemStack(Items.END_CRYSTAL)) || !mc.player.inventory.hasItemStack(new ItemStack(Blocks.OBSIDIAN))){
            MessageUtil.sendError("No obby/crystals found in the hotbar", -1117);
            onDisable();
            return;
        }

        upside = getUpsideBlock(target);
        switch (stage){
            case 1:
                //Block place stage
                if(BlockUtil.isAir(upside) && BlockUtil.isValid(upside)) {
                    if (timer.hasPassedMs(placeBlockDelay.getValue())) {
                        if(BlockUtil.getPossibleSides(upside).isEmpty()){
                            for(BlockPos pos : BlockUtil.getNeighbors(upside)){
                                if(BlockUtil.getPossibleSides(pos).isEmpty()) continue;
                                blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
                                stage = 2;
                                timer.reset();
                                break;
                            }
                        }else{
                            blockPlacer.placeBlock(upside, Blocks.OBSIDIAN);
                            stage = 2;
                            timer.reset();
                        }
                    }
                }else
                    stage = 2;
                break;
            case 2:
                //Crystal place stage
                EntityEnderCrystal placeCrystal = CrystalUtils.getCrystalAtPos(upside);
                if(placeCrystal == null) {
                    if(timer.hasPassedMs(placeCrystalDelay.getValue())){
                        itemUser.useItem(Items.END_CRYSTAL, upside, EnumHand.MAIN_HAND);
                        stage = 3;
                        timer.reset();
                    }
                }else
                    stage = 3;
                break;
            case 3:
                //Block break stage
                if(!BlockUtil.isAir(upside)){
                    if(timer.hasPassedMs(breakBlockDelay.getValue())){
                        blockBreaker.breakBlock(upside, Items.DIAMOND_PICKAXE);
                    }
                }else {
                    stage = 4;
                    timer.reset();
                }
                break;
            case 4:
                //Crystal break stage
                EntityEnderCrystal breakCrystal = CrystalUtils.getCrystalAtPos(upside);
                if(breakCrystal != null){
                    if(timer.hasPassedMs(breakCrystalDelay.getValue())){
                        Firework.rotationManager.rotateSpoof(breakCrystal.getPositionVector().add(0.5, 0.5, 0.5));
                        mc.playerController.attackEntity(mc.player, breakCrystal);
                        stage = 1;
                        timer.reset();
                    }
                }else
                    stage = 1;
                break;
        }
    });

    public int getStage(){
        int stage = 1;
        if(target == null) return stage;
        BlockPos upside = getUpsideBlock(target);
        EntityEnderCrystal crystal = CrystalUtils.getCrystalAtPos(upside);
        if(BlockUtil.isAir(upside) && crystal == null)
            stage = 1;
        else if(!BlockUtil.isAir(upside) && crystal == null)
            stage = 2;
        else if(!BlockUtil.isAir(upside) && crystal != null)
            stage = 3;
        else if(BlockUtil.isAir(upside) && crystal != null)
            stage = 4;
        return stage;
    }

    public BlockPos getUpsideBlock(EntityPlayer target){
        return EntityUtil.getFlooredPos(target).add(0, 2, 0);
    }
}
