package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "HoleEsp",category = Module.Category.RENDER)
public class HoleEsp extends Module {

    public Setting<Double> range = new Setting<>("Range", (double)10, this, 1, 20);


    public Setting<Enum> mode = new Setting<>("Mode", modes.Box, this, modes.values());
    public enum modes{
        Crosses, Box, GradientBox
    }

    public Setting<HSLColor> bedrockColor = new Setting<>("BedRock", new HSLColor(1, 54, 43), this);
    public Setting<HSLColor> obsidianColor = new Setting<>("Obsidian", new HSLColor(1, 54, 43), this);




    public Setting<HSLColor> startColorBebrok = new Setting<>("BedRockStart", new HSLColor(1, 54, 43), this).setVisibility(v-> mode.getValue(modes.GradientBox));
    public Setting<HSLColor> endColorBebrok = new Setting<>("BedrockEnd", new HSLColor(1, 54, 43), this).setVisibility(v-> mode.getValue(modes.GradientBox));

    public Setting<HSLColor> startColorObby = new Setting<>("BedRockStart", new HSLColor(1, 54, 43), this).setVisibility(v-> mode.getValue(modes.GradientBox));
    public Setting<HSLColor> endColorObby = new Setting<>("BedrockEnd", new HSLColor(1, 54, 43), this).setVisibility(v-> mode.getValue(modes.GradientBox));


    public Setting<Boolean> outline = new Setting<>("Outline", true, this).setVisibility(v-> mode.getValue(modes.Box));

    public Setting<Double> wight = new Setting<>("Wight", (double)10, this, 1, 20);
    public Setting<Boolean> square = new Setting<>("Square", true, this).setVisibility(v-> mode.getValue(modes.Crosses));
    public Setting<Boolean> box1 = new Setting<>("Box", true, this).setVisibility(v-> mode.getValue(modes.Crosses));

    public Setting<Double> height = new Setting<>("Height", (double)0.5, this, -1, 20).setVisibility(v-> mode.getValue(modes.Box));
    public Setting<Boolean> box = new Setting<>("Box", true, this).setVisibility(v-> mode.getValue(modes.Box));

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            if(mode.getValue(modes.Box)){
                RenderUtils.drawBoxESP(pos, this.isSafe(pos) ?
                        this.bedrockColor.getValue().toRGB() : this.obsidianColor.getValue().toRGB(),

                                wight.getValue().floatValue(),
                                this.outline.getValue(),
                                this.box.getValue(),
                                this.isSafe(pos) ? 200 : 199,
                                height.getValue().floatValue());
            }else if(mode.getValue(modes.Crosses)){
                RenderUtils.renderCrosses(pos, this.isSafe(pos) ?
                                this.bedrockColor.getValue().toRGB() : this.obsidianColor.getValue().toRGB(),
                                wight.getValue().floatValue());
            }else if(mode.getValue(modes.GradientBox)){


                if (isSafe(pos)) {
                new BlockRenderBuilder(pos)
                        .addRenderModes(
                                new RenderMode(RenderMode.renderModes.FilledGradient,
                                        startColorBebrok.getValue().toRGB(), endColorBebrok.getValue().toRGB())
                        ).render();

                } else if (!isSafe(pos)) {
                    new BlockRenderBuilder(pos)
                            .addRenderModes(
                                    new RenderMode(RenderMode.renderModes.FilledGradient,
                                           startColorObby.getValue().toRGB(), endColorObby.getValue().toRGB())
                            ).render();
                }


            }
            if(square.getValue() && mode.getValue(modes.Crosses)){
                RenderUtils.drawBoxESP(pos, this.isSafe(pos) ?
                                this.bedrockColor.getValue().toRGB() : this.obsidianColor.getValue().toRGB(),

                                wight.getValue().floatValue(),
                         true,
                                box1.getValue(),
                                this.isSafe(pos) ? 200 : 199,
                         0.01f);
            }
        }
    }



    //Holes Calculator
    @Override
    public void onTick(){
        super.onTick();
        this.holes = this.calcHoles();
    }

    private List<BlockPos> holes = new ArrayList<BlockPos>();
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

    private boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for (BlockPos offset : this.surroundOffset) {
            if (this.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock() == Blocks.BEDROCK) continue;
            isSafe = false;
            break;
        }
        return isSafe;
    }
}
