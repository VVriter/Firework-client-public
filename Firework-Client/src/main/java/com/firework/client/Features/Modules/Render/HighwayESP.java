package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@ModuleManifest(name = "HighwayESP",category = Module.Category.VISUALS)
public class HighwayESP extends Module {

    public Setting<Enum> page = new Setting<>("Page", pages.General, this, pages.values());
    public enum pages{
        General, Colors
    }

    public Setting<zeropoints> zeropoint = new Setting<>("ZeroPoint", zeropoints.FromYou, this, zeropoints.values()).setVisibility(v-> page.getValue(pages.General));
    public enum zeropoints{
        FromYou, ZeroCords
    }

    public Setting<ys> yzs = new Setting<>("Height", ys.FromYou, this, ys.values()).setVisibility(v-> page.getValue(pages.General));
    public enum ys{
        FromYou, Bedrock, Custom
    }
    public Setting<Double> lineWidth = new Setting<>("LineWidth", (double)3, this, 1, 10);
    public Setting<Double> yVal = new Setting<>("CustomHeight", (double)100, this, 0, 255).setVisibility(v-> page.getValue(pages.General));

    public Setting<HSLColor> color1 = new Setting<>("++", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));
    public Setting<HSLColor> color2 = new Setting<>("--", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));
    public Setting<HSLColor> color3 = new Setting<>("+-", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));
    public Setting<HSLColor> color4 = new Setting<>("-+", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));

    public Setting<HSLColor> color5 = new Setting<>("X+", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));
    public Setting<HSLColor> color6 = new Setting<>("X-", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));
    public Setting<HSLColor> color7 = new Setting<>("Z-", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));
    public Setting<HSLColor> color8 = new Setting<>("Z+", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Colors));


    BlockPos pos;
    int y;

    @Override
    public void onTick() {
        super.onTick();

        if (zeropoint.getValue(zeropoints.FromYou)) {
            pos = new BlockPos(mc.player.posX,y,mc.player.posZ);
        } else if (zeropoint.getValue(zeropoints.ZeroCords)) {
            pos = new BlockPos(0,y,0);
        }

        if (yzs.getValue(ys.FromYou)) {
            y = mc.player.getPosition().getY();
        } else if (yzs.getValue(ys.Bedrock)) {
            y = 1;
        } else if (yzs.getValue(ys.Custom)) {
            y = yVal.getValue().intValue();
        }

    }
    @SubscribeEvent
    public void render(RenderWorldLastEvent e) {
        BlockPos pos1 = new BlockPos(30000000,y,30000000);
        BlockPos pos2 = new BlockPos(-30000000,y,-30000000);
        BlockPos pos3 = new BlockPos(30000000,y,-30000000);
        BlockPos pos4 = new BlockPos(-30000000,y,30000000);

        BlockPos pos5 = new BlockPos(30000000,y,0);
        BlockPos pos6 = new BlockPos(-30000000,y,0);
        BlockPos pos7 = new BlockPos(0,y,-30000000);
        BlockPos pos8 = new BlockPos(0,y,30000000);

        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos1),lineWidth.getValue().intValue(),color1.getValue().toRGB());
        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos2),lineWidth.getValue().intValue(),color2.getValue().toRGB());
        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos3),lineWidth.getValue().intValue(),color3.getValue().toRGB());
        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos4),lineWidth.getValue().intValue(),color4.getValue().toRGB());

        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos5),lineWidth.getValue().intValue(),color5.getValue().toRGB());
        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos6),lineWidth.getValue().intValue(),color6.getValue().toRGB());
        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos7),lineWidth.getValue().intValue(),color7.getValue().toRGB());
        RenderUtils.drawLine(BlockUtil.posToVec3d(pos),BlockUtil.posToVec3d(pos8),lineWidth.getValue().intValue(),color8.getValue().toRGB());
    }
}
