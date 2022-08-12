package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.UI.ConfigUi.CGui;

@ModuleManifest(
        name = "ConfigGui",
        category = Module.Category.CLIENT,
        description = "Gegra"
)
public class ConfigGui extends Module {
    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(new CGui());
    }
}
