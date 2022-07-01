package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.GuiNEO.Particles.ParticleInfo;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(name = "GuiParticles", category = Module.Category.CLIENT)
public class GuiParticles extends Module {
    public static Setting<Double> scaleFactor;
    public static Setting<Double> lineLong;
    public static Setting<ParticleInfo.colorMode> colorMode;

    public GuiParticles(){
        scaleFactor = new Setting<>("scaleFactor", (double)0.4, this, 0.1, 10);
        lineLong = new Setting<>("maxLineLong", (double)90, this, 0, 200);
        colorMode = new Setting<>("ColorMode", ParticleInfo.colorMode.Default, this, ParticleInfo.colorMode.values());
        this.isEnabled.setValue(true);
        ParticleInfo.isEnabled = this.isEnabled.getValue();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        ParticleInfo.mode = colorMode.getValue();
    }

    @Override
    public void onToggle() {
        super.onToggle();
        ParticleInfo.isEnabled = this.isEnabled.getValue();
    }
}
