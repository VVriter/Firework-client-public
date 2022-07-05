package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@ModuleManifest(name = "CityESP", category = Module.Category.RENDER)
public class CityESP extends Module{

    public Setting<HSLColor> obbyColor = new Setting<>("Obby", new HSLColor(1 ,50, 50), this);
    public Setting<HSLColor> bedrockColor = new Setting<>("Bedrock", new HSLColor(1 ,50, 50), this);

    public Setting<Integer> targetRange = new Setting<>("TargetRange", 4, this,0, 30);

    public EntityPlayer entityTarget;

    @Override
    public void onTick() {
        super.onTick();
        entityTarget = PlayerUtil.getClosestTarget(targetRange.getValue());
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(entityTarget == null) return;
        for(BlockPos pos : getBlocksToPlace()){
            if(BlockUtil.getBlock(pos) == Blocks.OBSIDIAN)
                new BlockRenderBuilder(pos)
                        .addRenderModes(
                                new RenderMode(RenderMode.renderModes.FilledGradient,
                                        new Color(RainbowUtil.astolfoColors(100, 100)), new Color(RainbowUtil.astolfoColors(150, 100)))
                        ).render();
            if(BlockUtil.getBlock(pos) == Blocks.BEDROCK)
                new BlockRenderBuilder(pos)
                        .addRenderModes(
                                new RenderMode(RenderMode.renderModes.FilledGradient,
                                        new Color(RainbowUtil.astolfoColors(100, 100)), new Color(RainbowUtil.astolfoColors(150, 100)))
                        ).render();
        }
    }

    public BlockPos[] getBlocksToPlace() {
        BlockPos p = EntityUtil.getFlooredPos(entityTarget);
        return new BlockPos[]{p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
    }
}
