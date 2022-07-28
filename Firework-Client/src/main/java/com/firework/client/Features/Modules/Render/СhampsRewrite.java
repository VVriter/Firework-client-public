package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(
        name = "ChampsRewrite",
        category = Module.Category.VISUALS,
        description = "Cool render (better than ESP)"
)
public class СhampsRewrite extends Module {
    public static Setting<Boolean> enabled = null;

    public СhampsRewrite(){
        enabled = this.isEnabled;
    }

}
