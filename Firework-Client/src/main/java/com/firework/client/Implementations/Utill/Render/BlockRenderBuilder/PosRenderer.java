package com.firework.client.Implementations.Utill.Render.BlockRenderBuilder;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.awt.*;


public class PosRenderer {

    private Module module;
    private Setting<PosRenderer.renderModes> renderMode;
    private Setting<PosRenderer.boxeMode> boxModes;
    private Setting<PosRenderer.outlineModes> outlineMode;

    public PosRenderer(Module module, Setting renderMode, Setting boxModes, Setting outlineMode){
        this.module = module;
        this.renderMode = renderMode;
        this.boxModes = boxModes;
        this.outlineMode = outlineMode;
    }

    public void doRender(
            BlockPos posTorender,
            Color outlineColor,
            Color outlineColor1,
            Color outlineColor2,
            Color fillColor,
            Color fillColorStart,
            Color fillColorEnd,
            int width,
            float heightBox,
            float heightOutline,
            Color beaconFillColor,
            int beaconOutlineWidth
    )


    {
        if (renderMode.getValue(renderModes.Box)) {
            if (boxModes.getValue(boxeMode.Normal)) {
                RenderUtils.drawBoxESP(posTorender,fillColor,width,false,true,fillColor.getAlpha(),heightBox);
            } else if (boxModes.getValue(boxeMode.Gradient)) {
                RenderUtils.drawGradientFilledBox(posTorender,fillColorStart,fillColorEnd);
            }


            if (outlineMode.getValue(outlineModes.Normal)) {
               RenderUtils.drawBoxESP(
                       posTorender,
                       outlineColor,
                       width,
                       true,
                       false,
                       0,
                       heightOutline
                       );
            } else if (outlineMode.getValue(outlineModes.Gradient)) {
               RenderUtils.drawGradientBlockOutline(
                       posTorender,
                       outlineColor1,
                       outlineColor2,
                       width
               );
            }
        }


        else if (renderMode.getValue(renderModes.Beacon)) {
            RenderUtils.drawBoxESP(posTorender,beaconFillColor,beaconOutlineWidth,true,false,0,1);
            RenderUtils.drawBoxESP(posTorender,beaconFillColor,beaconOutlineWidth,true,true,beaconFillColor.getAlpha(), Minecraft.getMinecraft().world.getHeight());
        }
    }

    private void updateSettings(){
        this.renderMode = Firework.settingManager.getSetting(module, renderMode.name);
    }


    public enum boxeMode{
        Normal, Gradient, None
    }

    public enum outlineModes{
        Normal, Gradient, None
    }

    public enum renderModes{
        Box, Beacon
    }
}
