package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BoundingBoxUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.RenderText;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@ModuleManifest(name = "BlockHighlight",category = Module.Category.RENDER)
public class BlockHighlight extends Module {

    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 54, 43), this);
    public Setting<Double> height = new Setting<>("Height", (double)1, this, -0.3, 1.5);
    public Setting<Double> width = new Setting<>("Width", (double)3, this, 1, 10);
    public Setting<Boolean> outline = new Setting<>("Outline", true, this);
    public Setting<Boolean> box = new Setting<>("Box", true, this);
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        RayTraceResult mouseOver = mc.objectMouseOver;
        if (mouseOver != null && mouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
            BlockPos pos = mouseOver.getBlockPos();
         /*  RenderUtils.drawBoxESP(pos,new Color(color.getValue().toRGB().getRed(),
                                color.getValue().toRGB().getGreen(),
                                color.getValue().toRGB().getBlue()),
                                width.getValue().floatValue(),
                                outline.getValue(),box.getValue(),
                        200,height.getValue().floatValue()); */

            new BlockRenderBuilder(pos)
                    .addRenderModes(
                            new RenderMode(RenderMode.renderModes.FilledGradient,
                                    new Color(RainbowUtil.astolfoColors(100, 100)), new Color(RainbowUtil.astolfoColors(150, 100)))
                    ).render();



            RenderText.drawText(pos,"Hello World!");
        }
    }
}
