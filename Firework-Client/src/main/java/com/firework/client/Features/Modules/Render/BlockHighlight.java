package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@ModuleManifest(name = "BlockHighlight",category = Module.Category.RENDER)
public class BlockHighlight extends Module {

    PosRenderer posRenderer;

    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("RenderMode", PosRenderer.renderModes.Beacon, this, PosRenderer.renderModes.values());
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("BoxMode", PosRenderer.boxeMode.Normal, this, PosRenderer.boxeMode.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box));
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box));
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box));
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box));
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box));



    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("OutlineMode", PosRenderer.outlineModes.Normal, this, PosRenderer.outlineModes.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box));
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient));
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient));
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal));
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal));
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && !outlineMode.getValue(PosRenderer.outlineModes.None));
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        RayTraceResult result = mc.objectMouseOver;
        if (result != null && result.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
           BlockPos  pos = result.getBlockPos();
            posRenderer.doRender(
                    pos,
                    colorOutline.getValue().toRGB(),
                    gradientOutlineColor1.getValue().toRGB(),
                    gradientOutlineColor2.getValue().toRGB(),
                    fillColor.getValue().toRGB(),
                    fillColor1.getValue().toRGB(),
                    fillColor2.getValue().toRGB(),
                    outlineWidth.getValue(),
                    boxHeightNormal.getValue().floatValue(),
                    outlineHeightNormal.getValue().floatValue()
            );
        }
    }


    @Override
    public void onEnable(){
        super.onEnable();
        posRenderer = new PosRenderer(this,renderMode,boxMode,outlineMode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        posRenderer = null;
    }
}
