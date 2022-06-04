package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Gui.Particles.ParticleInfo;

@ModuleArgs(name = "GuiParticles", category = Module.Category.CLIENT)
public class GuiParticles extends Module {

    @Override
    public void onToggle() {
        super.onToggle();
        ParticleInfo.isEnabled = this.isEnabled.getValue();
    }
}
