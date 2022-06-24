package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "HoleESP",category = Module.Category.RENDER)
public class HoleESP extends Module {


    static boolean down;


    public Setting<Double> range = new Setting<>("Range", (double)10, this, 1, 20);


    public Setting<Enum> mode = new Setting<>("RenderMode", modes.Crosses, this, modes.values());
    public enum modes{
        Crosses, Box
    }

    public Setting<HSLColor> color = new Setting<>("BedrockColor", new HSLColor(1, 54, 43), this);
    public Setting<HSLColor> color1 = new Setting<>("ObsidianColor", new HSLColor(1, 54, 43), this);

    public Setting<Boolean> outline = new Setting<>("Outline", false, this);
    public Setting<Boolean> box = new Setting<>("Box", false, this);
    public Setting<Boolean> flat = new Setting<>("Flat", false, this);
    public Setting<Boolean> wireframe = new Setting<>("Wireframe", false, this);

    public Setting<Double> bedRockAlpha = new Setting<>("BedRockAlpha", (double)3, this, 1, 10);
    public Setting<Double> obsidianAlpha = new Setting<>("ObsidianAlpha", (double)3, this, 1, 10);

    public Setting<Double> wight = new Setting<>("LineWight", (double)2, this, 1, 10);


   /* public Setting<updowns> updown = new Setting<>("Mode", updowns.Down, this, updowns.values()).setVisibility(mode, modes.Crosses);
    public enum updowns{
        Up, Down
    }*/
    public Setting<Double> hihgt = new Setting<>("Height", (double)1, this, 0, 10);

    @Override
    public void onTick(){
        super.onTick();
        this.holes = this.calcHoles();
    }


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            Color color = this.isSafe(pos) ? new Color(this.color.getValue().toRGB().getRed(), this.color.getValue().toRGB().getGreen(),this.color.getValue().toRGB().getBlue()) : new Color(this.color1.getValue().toRGB().getRed(), this.color1.getValue().toRGB().getGreen(),this.color1.getValue().toRGB().getBlue());
            RenderUtils.drawBoxESP(pos, color, wight.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.isSafe(pos) ? this.bedRockAlpha.getValue().intValue() : this.obsidianAlpha.getValue().intValue(), this.flat.getValue() != false ? 0.0f : hihgt.getValue().floatValue());
            if (!this.wireframe.getValue().booleanValue()) continue;
            RenderUtils.renderCrosses(pos, color,  wight.getValue().floatValue());
        }
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
