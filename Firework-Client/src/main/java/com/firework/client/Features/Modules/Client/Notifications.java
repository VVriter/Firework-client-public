package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render2dE;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.gui.ScaledResolution;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Notifications",
        category = Module.Category.CLIENT
)
public class Notifications extends Module {
    public static Setting<Integer> colorInterpolation = null;

    public Notifications() {
        colorInterpolation =  new Setting<>("colorInterpolation", 3, this, 1, 5);
    }
    ScaledResolution sr = new ScaledResolution(mc);

    @Subscribe
    public Listener<Render2dE> listener = new Listener<>(e-> {

    });
}
