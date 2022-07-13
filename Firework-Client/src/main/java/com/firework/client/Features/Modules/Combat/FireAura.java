package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Utill.Timer;

@ModuleManifest(name = "FireAura",category = Module.Category.COMBAT)
public class FireAura extends Module {
    boolean shouldDoAutoCrystal;

    Timer timer = new Timer();

    @Override public void onEnable() {super.onEnable();

    }
}
