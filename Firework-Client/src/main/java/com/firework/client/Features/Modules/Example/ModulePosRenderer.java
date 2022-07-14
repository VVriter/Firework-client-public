package com.firework.client.Features.Modules.Example;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(
        name = "ModulePosRenderer",
        category = Module.Category.EXAMPLE
)
public class ModulePosRenderer extends Module {
    PosRenderer posRenderer;
    BlockPos posTorender;

    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("SwitchMode", PosRenderer.renderModes.Beacon, this, PosRenderer.renderModes.values());
    public Setting<HSLColor> colorSetting = new Setting<>("colorN", new HSLColor(1, 54, 43), this);
    public Setting<HSLColor> gradCo1 = new Setting<>("Color1", new HSLColor(1, 54, 43), this);
    public Setting<HSLColor> gradCo2 = new Setting<>("Color2", new HSLColor(200, 54, 43), this);
    public Setting<Double> width = new Setting<>("Width", (double)3, this, 1, 10);


    @Override
    public void onEnable() {
        super.onEnable();
        posRenderer = new PosRenderer(this,renderMode);
        posTorender = mc.player.getPosition();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        posRenderer = null;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (posRenderer != null) {
            posRenderer.doRender(
                    posTorender,
                    colorSetting.getValue().toRGB(),
                    width.getValue().floatValue(),
                    gradCo1.getValue().toRGB(),
                    gradCo2.getValue().toRGB()
            );
        }
    }
}
