package com.firework.client.Features.Modules.Render.TracersModule;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "TracersRewrote",category = Module.Category.RENDER)
public class Tracers extends Module {

    public Setting<Double> range = new Setting<>("tD", (double)100, this, 1, 500);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {

    }


}
