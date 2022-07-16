package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "Tracers",category = Module.Category.RENDER)
public class Tracers extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Tracers, this, modes.values());
    public enum modes{
        Tracers, Arrows
    }

    public Setting<Double> width = new Setting<>("Width", (double)3, this, 1, 10).setVisibility(v-> mode.getValue(modes.Tracers));
    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 54, 43), this);


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        for (Entity playerEntity : mc.world.playerEntities) {
            if (playerEntity != null && playerEntity != mc.player) {
                if(mode.getValue(modes.Tracers)){
                RenderUtils.trace(mc, playerEntity, mc.getRenderPartialTicks(),1, width.getValue().floatValue());
                }
            }
        }
    }



}
