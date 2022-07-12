package com.firework.client.Features.Modules.Combat.Rewrite.HoleFiller;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.WorldClientInitEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Blocks.HoleUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;

@ModuleManifest(name = "HolleFillerRewrite", category = Module.Category.COMBAT)
public class HoleFiller extends Module {

    private Setting<Integer> radius = new Setting<>("Radius", 0, this, 0, 10);

    private Setting<Integer> placedDelayMs = new Setting<>("PlaceDelayMs", 0, this, 0, 100);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this, BlockPlacer.switchModes.values());

    private Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    private Setting<Boolean> packet = new Setting<>("Packet", true, this);

    private Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 50, 50), this);

    private Timer placeTimer;

    private BlockPlacer blockPlacer;

    private ArrayList<BlockPos> line;

    @Override
    public void onEnable() {
        super.onEnable();
        placeTimer = new Timer();
        placeTimer.reset();

        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);

        line = new ArrayList<>();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        blockPlacer = null;

        placeTimer.reset();

        line = null;
    }

    @Override
    public void onTick() {
        super.onTick();

        for(BlockPos pos : HoleUtil.calculateSingleHoles(radius.getValue())){
            if(isAir(pos) && !line.contains(pos) && pos != EntityUtil.getFlooredPos(mc.player))
                line.add(pos);
        }

        ArrayList<BlockPos> placedBlocks = new ArrayList<>();

        for(BlockPos pos : line){
            if(placeTimer.hasPassedMs(placedDelayMs.getValue())){
                blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
                placedBlocks.add(pos);
                placeTimer.reset();
            }else {
                break;
            }
        }

        line.removeAll(placedBlocks);
    }

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event){
        if(line == null) return;
        for(BlockPos pos : line){
            new BlockRenderBuilder(pos)
                    .addRenderModes(
                            new RenderMode(RenderMode.renderModes.Fill, color.getValue().toRGB())
                    ).render();
        }
    }

    @SubscribeEvent
    public void onWorldJoin(WorldClientInitEvent event) {
        line.clear(); line = null;
        blockPlacer = null;
        placeTimer.reset(); placeTimer = null;
    }

    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }
}
