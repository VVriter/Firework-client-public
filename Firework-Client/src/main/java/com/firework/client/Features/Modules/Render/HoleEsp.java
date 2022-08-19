package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Blocks.BoundingBoxUtil;
import com.firework.client.Implementations.Utill.Blocks.HoleUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(
        name = "HoleEsp",
        category = Module.Category.VISUALS,
        description = "Renders safe bedrock/obsidian holes"
)
public class HoleEsp extends Module {
    public Setting<Integer> range = new Setting<>("Range", 10, this, 1, 30);
    public Setting<Double> height = new Setting<>("Height", (double)0.1, this, 0, 1);
    public Setting<Boolean> outlineSubBool = new Setting<>("Outline", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> outlineEnable = new Setting<>("OutlineEnable", true, this).setVisibility(()-> outlineSubBool.getValue());
    public Setting<Double> outlineThickness = new Setting<>("OutlineThickness", (double)3, this, 1, 10).setVisibility(()-> outlineSubBool.getValue());
    public Setting<HSLColor> outlineBedrockStartColor = new Setting<>("BedrockStartColor", new HSLColor(1, 54, 43), this).setVisibility(()-> outlineSubBool.getValue());
    public Setting<HSLColor> outlineBedrockEndColor = new Setting<>("BedrockEndColor", new HSLColor(50, 54, 43), this).setVisibility(()-> outlineSubBool.getValue());
    public Setting<HSLColor> outlineObsidianStartColor = new Setting<>("ObsidianStartColor", new HSLColor(100, 54, 43), this).setVisibility(()-> outlineSubBool.getValue());
    public Setting<HSLColor> outlineObsidianEndColor = new Setting<>("ObsidianEndColor", new HSLColor(150, 54, 43), this).setVisibility(()-> outlineSubBool.getValue());

    public Setting<Boolean> glowSubBool = new Setting<>("Glow", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> enableGlow = new Setting<>("EnableGlow", true, this).setVisibility(()-> glowSubBool.getValue());
    public Setting<GlowMode> glowMode = new Setting<>("GlowMode", GlowMode.Down, this).setVisibility(()-> glowSubBool.getValue());
    public enum GlowMode{ Up, Down }
    public Setting<HSLColor> glowBedrockColor = new Setting<>("GlowBedrockColor", new HSLColor(1, 54, 43), this).setVisibility(()-> glowSubBool.getValue());
    public Setting<HSLColor> glowObsidianColor = new Setting<>("GlowObsidianColor", new HSLColor(50, 54, 43), this).setVisibility(()-> glowSubBool.getValue());


    public Setting<Boolean> colors = new Setting<>("Colors", false, this).setMode(Setting.Mode.SUB);
    public Setting<HSLColor> fillColorBedrock = new Setting<>("FillColorBedrock", new HSLColor(1, 54, 43), this).setVisibility(()-> colors.getValue());
    public Setting<HSLColor> fillColorObsidian = new Setting<>("FillColorObsidian", new HSLColor(50, 54, 43), this).setVisibility(()-> colors.getValue());

    @Subscribe
    public Listener<Render3dE> listener2 = new Listener<>(event -> {

        for (BlockPos pos : HoleUtil.calculateHoles(range.getValue(),false,true)) {
            RenderUtils.drawBoxESP(pos,Color.RED,1,true,true,155,1);
        }


        for (BlockPos pos : calcHoles()) {
                if (isSafe(pos)) {
                    //Glow
                    if (enableGlow.getValue()) {
                        if (glowMode.getValue(GlowMode.Down)) {
                            RenderUtils.drawGradientFilledBox(pos,glowBedrockColor.getValue().toRGB(),new Color(1,1,1,0));
                        } else if (glowMode.getValue(GlowMode.Up)) {
                            RenderUtils.drawGradientFilledBox(pos,new Color(1,1,1,0),glowBedrockColor.getValue().toRGB());
                        }
                    }

                    //Outline
                    if (outlineEnable.getValue()) {
                        RenderUtils.drawGradientBlockOutlineWithHeight(BoundingBoxUtil.getBB(pos,height.getValue()),outlineBedrockStartColor.getValue().toRGB(),outlineBedrockEndColor.getValue().toRGB(),outlineThickness.getValue().floatValue());
                    }

                    //Fill
                    RenderUtils.drawBoxESP(pos,fillColorBedrock.getValue().toRGB(),1,false,true,fillColorBedrock.getValue().toRGB().getAlpha(),height.getValue().floatValue());
                } else if (!isSafe(pos)) {
                    //Glow
                    if (enableGlow.getValue()) {
                        if (glowMode.getValue(GlowMode.Down)) {
                            RenderUtils.drawGradientFilledBox(pos,glowObsidianColor.getValue().toRGB(),new Color(1,1,1,0));
                        } else if (glowMode.getValue(GlowMode.Up)) {
                            RenderUtils.drawGradientFilledBox(pos,new Color(1,1,1,0),glowObsidianColor.getValue().toRGB());
                        }
                    }

                    if (outlineEnable.getValue()) {
                        RenderUtils.drawGradientBlockOutlineWithHeight(BoundingBoxUtil.getBB(pos,height.getValue()),outlineObsidianStartColor.getValue().toRGB(),outlineObsidianEndColor.getValue().toRGB(),outlineThickness.getValue().floatValue());
                    }

                    //Fill
                    RenderUtils.drawBoxESP(pos,fillColorObsidian.getValue().toRGB(),1,false,true,fillColorBedrock.getValue().toRGB().getAlpha(),height.getValue().floatValue());
                }
            }
        }
         );


    private boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for (BlockPos offset : this.surroundOffset) {
            if (this.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock() == Blocks.BEDROCK) continue;
            isSafe = false;
            break;
        }
        return isSafe;
    }

    private final BlockPos[] surroundOffset = BlockUtil.toBlockPos(BlockUtil.holeOffsets);

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), false);
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
}
