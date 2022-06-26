package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.World.Burrow.SelfBlock;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
@ModuleManifest(name = "HoleFiller",category = Module.Category.COMBAT)
public class HoleFiller extends Module {
    public Setting<Enum> mode = new Setting<>("Mode", modes.Normal, this, modes.values());
    public enum modes{
        Normal, Smart
    }
    public Setting<Double> distance = new Setting<>("Distance", (double)5, this, 1, 10);
    public Setting<Double> tickDelay = new Setting<>("Delay", (double)1, this, 1, 100);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this).setVisibility(rotate,true).setVisibility(rotate,true);
    public Setting<Boolean> shuldDisableOnJump = new Setting<>("DisableOnJump", true, this);
    public Setting<Boolean> shuldDisableOnOk = new Setting<>("DisableOnFill", true, this);

    public Setting<Boolean> autoBurrow = new Setting<>("AutoBurrow", true, this);
    public Setting<HSLColor> renderColor = new Setting<>("RenderColor", new HSLColor(1, 54, 43), this);


    Timer timer;
    @Override
    public void onEnable(){
        super.onEnable();
        timer = new Timer();
        timer.reset();
       /* if(autoBurrow.getValue()
                && mc.player.onGround
                && !mc.player.isInLava()
                && !mc.player.isInWater()){
            SelfBlock.enabled.setValue(true);
        }*/
    }


    @Override
    public void onTick(){
        super.onTick();
        //OnTick calcs holes
        this.holes = this.calcHoles();
        //AutoDisable
        if(shuldDisableOnOk.getValue() && this.holes.isEmpty()){
            onDisable();
        }
        if(mode.getValue(modes.Normal)){
            if(timer.passedS(tickDelay.getValue())) {
                makeNormalSwitch();
                makeHoleFill();
                timer.reset();
            }
        }else if(mode.getValue(modes.Smart)){
            for (Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityPlayer && e != mc.player) {
                    if (mc.player.getDistance(e) <= distance.getValue() && mc.player.getDistance(e) <= distance.getValue() ) {
                        makeHoleFill();
                    }
                }
            }
        }
    }
    //OnJump disable
    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            if(shuldDisableOnJump.getValue()){onDisable();}
        }
    }
    //Hole calculator
    private List<BlockPos> holes = new ArrayList<BlockPos>();
    private final BlockPos[] surroundOffset = BlockUtil.toBlockPos(BlockUtil.holeOffsets);
    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.distance.getValue().floatValue(), false);
        int size = positions.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = positions.get(i);
            if (!this.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !this.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !this.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) continue;
            boolean isSafe = true;
            for (BlockPos offset : this.surroundOffset) {
                Block block = this.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) continue;
                isSafe = false;
            }
            if (!isSafe) continue;
            safeSpots.add(pos);
        }
        return safeSpots;
    }
    private boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for (BlockPos offset : this.surroundOffset) {
            if (this.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock() == Blocks.BEDROCK) continue;
            isSafe = false;
            break;
        }
        return isSafe;
    }
    //HoleFill code
    public void makeHoleFill() {
        //HoleFillCode
        MessageUtil.sendClientMessage("Im Holefiling now", -1117);
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            BlockUtil.placeBlock(pos,EnumHand.MAIN_HAND,rotate.getValue(),packet.getValue(),false);
        }
    }
    //Switch code
    public void makeNormalSwitch(){
        InventoryUtil.doMultiHand(new ItemStack(Blocks.OBSIDIAN).getItem(),InventoryUtil.hands.MainHand);
    }
    //Render
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            RenderUtils.drawBoxESP(pos,new Color(renderColor.getValue().toRGB().getRed(),renderColor.getValue().toRGB().getGreen(),renderColor.getValue().toRGB().getBlue()),5,true,true,200,1);
        }
    }
}