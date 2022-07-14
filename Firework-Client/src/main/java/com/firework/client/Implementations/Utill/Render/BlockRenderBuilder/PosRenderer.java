package com.firework.client.Implementations.Utill.Render.BlockRenderBuilder;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class PosRenderer {
    private Module module;
    private Setting<PosRenderer.renderModes> renderMode;

    public PosRenderer(Module module, Setting renderMode){
        this.module = module;
        this.renderMode = renderMode;
    }

    public void doRender(BlockPos posTorender, Color color, float with) {
        if (posTorender != null) {
            if (renderMode.getValue(renderModes.OutLine)) {
                new BlockRenderBuilder(posTorender)
                        .addRenderModes(
                                new RenderMode(RenderMode.renderModes.OutLine,
                                        color,
                                        with)
                        ).render();
            }  else if (renderMode.getValue(renderModes.Fill)) {
                new BlockRenderBuilder(posTorender)
                        .addRenderModes(
                                new RenderMode(RenderMode.renderModes.Fill,
                                        color)
                        ).render();
            }  else if (renderMode.getValue(renderModes.Beacon)) {
                new BlockRenderBuilder(posTorender)
                        .addRenderModes(
                                new RenderMode(RenderMode.renderModes.Beacon,
                                        color)
                        ).render();
            }
        }
    }

    //Update settings
    private void updateSettings(){
        this.renderMode = Firework.settingManager.getSetting(module, renderMode.name);
    }

    //Switch modes
    public enum renderModes{
        OutLine, Fill, OutlineGradient, FilledGradient, Beacon
    }
}
