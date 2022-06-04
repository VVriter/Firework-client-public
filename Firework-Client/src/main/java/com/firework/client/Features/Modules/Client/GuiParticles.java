package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Gui.Particles.ParticleInfo;
import com.firework.client.Implementations.Settings.Setting;

@ModuleArgs(name = "GuiParticles", category = Module.Category.CLIENT)
public class GuiParticles extends Module {
    public static Setting<Double> scaleFactor;
    public static Setting<Double> lineLong;

    public GuiParticles(){
        scaleFactor = new Setting<>("scaleFactor", (double)1, this, 0.1, 10);
        lineLong = new Setting<>("maxLineLong", (double)1, this, 0, 100);
    }

    @Override
    public void onToggle() {
        super.onToggle();
        ParticleInfo.isEnabled = this.isEnabled.getValue();
    }
}
