package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render3DEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleManifest(name = "CrystalSidesESP",category = Module.Category.RENDER)
public class LavaESP extends Module {

    public Setting<Double> hihgt = new Setting<>("Height", (double)2, this, 1, 10);
    public Setting<Double> wight = new Setting<>("LineWight", (double)2, this, 1, 10);
    public Setting<Boolean> Water = new Setting<>("Water", true, this);
    public Setting<HSLColor> waterColor = new Setting<>("WaterColor", new HSLColor(7, 54, 43), this).setVisibility(Water,true);

    public Setting<Boolean> Lava = new Setting<>("Lava", true, this);
    public Setting<HSLColor> lavaColor = new Setting<>("LavaColor", new HSLColor(7, 54, 43), this).setVisibility(Lava,true);




    List<BlockPos> airBlockList = new ArrayList<BlockPos>();

    List<BlockPos> airBlockList2 = new ArrayList<BlockPos>();

    @Override
    public void onUpdate() {
        int lavaCount = 0;
        int renderBlocks = 0;
        this.airBlockList.clear();
        List<BlockPos> sortedSphere = BlockUtil.getSphere(8.0f, false);
        sortedSphere.sort(Comparator.comparingDouble(pos -> -this.mc.player.getDistanceSq(pos)));
        for (BlockPos pos2 : BlockUtil.getSphere(8.0f, false)) {
            if (this.mc.world.getBlockState(pos2).getMaterial() == Material.LAVA) {
                ++lavaCount;
            }
            if (lavaCount < 10 || !BlockUtil.canPlaceCrystal(pos2, true)) continue;
            if (++renderBlocks > 8) break;
            this.airBlockList.add(pos2);
        }

        int lavaCount5 = 0;
        int renderBlocks5 = 0;
        this.airBlockList2.clear();
        List<BlockPos> sortedSphere5 = BlockUtil.getSphere(8.0f, false);
        sortedSphere5.sort(Comparator.comparingDouble(pos -> -this.mc.player.getDistanceSq(pos)));
        for (BlockPos pos5 : BlockUtil.getSphere(8.0f, false)) {
            if (this.mc.world.getBlockState(pos5).getMaterial() == Material.WATER) {
                ++lavaCount5;
            }
            if (lavaCount5 < 10 || !BlockUtil.canPlaceCrystal(pos5,true)) continue;
            if (++renderBlocks5 > 8) break;
            this.airBlockList2.add(pos5);
        }
    }



    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        for (BlockPos pos : this.airBlockList) {
            if(Lava.getValue()){
            RenderUtils.drawBoxESP(pos, new Color(lavaColor.getValue().toRGB().getRed(), lavaColor.getValue().toRGB().getGreen(), lavaColor.getValue().toRGB().getBlue(), 255), wight.getValue().floatValue(), true, true, 70, hihgt.getValue().floatValue());}
            for (BlockPos pos5 : this.airBlockList2) {
            if(Water.getValue()){
                RenderUtils.drawBoxESP(pos5, new Color(waterColor.getValue().toRGB().getRed(), waterColor.getValue().toRGB().getGreen(), waterColor.getValue().toRGB().getBlue(), 255), wight.getValue().floatValue(), true, true, 70, hihgt.getValue().floatValue());}
        }
    }
}}
