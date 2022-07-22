package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;

@ModuleManifest(name = "CevBreaker", category = Module.Category.COMBAT)
public class CevBreaker extends Module {

    public Setting<Integer> targetRange = new Setting<>("TargetRange", 3, this, 1, 6);

    public Setting<Boolean> items = new Setting<>("Items", false, this).setMode(Setting.Mode.SUB);
    public Setting<ItemUser.switchModes> itemSwitch = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this).setVisibility(v-> items.isVisible());
    public Setting<Boolean> itemRotate = new Setting<>("Rotate", true, this).setVisibility(v-> items.isVisible());

    public Setting<Boolean> blocks = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
    private Setting<BlockPlacer.switchModes> blockSwitch = new Setting<>("PlaceSwitch", BlockPlacer.switchModes.Fast, this).setVisibility(v-> blocks.isVisible());
    private Setting<BlockBreaker.mineModes> breakMode = new Setting<>("BreakMode", BlockBreaker.mineModes.Classic, this).setVisibility(v-> blocks.isVisible());
    private Setting<Boolean> blockRayTrace = new Setting<>("RayTrace", true, this).setVisibility(v-> blocks.isVisible());
    private Setting<Boolean> blockRotate = new Setting<>("Rotate", true, this).setVisibility(v-> blocks.isVisible());
    private Setting<Boolean> blockPacket = new Setting<>("Packet", false, this).setVisibility(v-> blocks.isVisible());

    public Setting<Integer> delay = new Setting<>("DelayMs", 3, this, 1, 100);

    EntityPlayer target;

    ItemUser itemUser;
    BlockPlacer blockPlacer;
    BlockBreaker blockBreaker;
    Timer timer;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) onDisable();

        timer = new Timer();
        itemUser = new ItemUser(this, itemSwitch, itemRotate);
        blockPlacer = new BlockPlacer(this, blockSwitch, blockRotate, blockPacket);
        blockBreaker = new BlockBreaker(this, breakMode, blockRotate, blockRayTrace, blockPacket);
        Firework.eventBus.register(listener1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Firework.eventBus.unregister(listener1);
        blockBreaker = null;
        blockPlacer = null;
        itemUser = null;
    }

    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        this.target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(this.target == null) return;
        doCevBreak();
    });

    public void doCevBreak(){
        if(!mc.player.inventory.hasItemStack(new ItemStack(Items.END_CRYSTAL)) || !mc.player.inventory.hasItemStack(new ItemStack(Blocks.OBSIDIAN))){
            MessageUtil.sendError("No obby/crystals found in the hotbar", -1117);
            return;
        }
        BlockPos upside = getUpsideBlock(target);
        if(BlockUtil.isAir(upside) && BlockUtil.isValid(upside)) {
            //Blow the crystal
            if(CrystalUtils.getCrystalAtPos(upside) != null)
                mc.playerController.attackEntity(mc.player, CrystalUtils.getCrystalAtPos(upside));
            //Place block
            if(BlockUtil.getPossibleSides(upside).isEmpty()){
                for(BlockPos pos : BlockUtil.toBlockPos(BlockUtil.getHelpingBlocks(BlockUtil.posToVec3d(upside)))){
                    if(BlockUtil.getPossibleSides(pos).isEmpty()) continue;
                    blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
                }
            }else{
                blockPlacer.placeBlock(upside, Blocks.OBSIDIAN);
            }
        }else{
            if(BlockUtil.canPlaceCrystal(upside))
                itemUser.useItem(Items.END_CRYSTAL, upside, EnumHand.MAIN_HAND);
            else
                blockBreaker.breakBlock(upside, Items.DIAMOND_PICKAXE);
        }
    }

    public BlockPos getUpsideBlock(EntityPlayer target){
        return EntityUtil.getFlooredPos(target).add(0, 2, 0);
    }
}
