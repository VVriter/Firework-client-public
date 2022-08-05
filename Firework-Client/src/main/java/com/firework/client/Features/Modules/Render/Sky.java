package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(
        name = "Fov/Ratio",
        category = Module.Category.VISUALS
)
public class Sky extends Module {
    public static Setting<Integer> worldX = null;
    public static Setting<Integer> worldY = null;
    public static Setting<Boolean> enabled = null;

    public Sky() {
        enabled = this.isEnabled;
        worldX = new Setting<>("SkyScaleX", 3, this, 1, 1920);
        worldY = new Setting<>("SkyScaleY", 3, this, 1, 1920);
    }
}
