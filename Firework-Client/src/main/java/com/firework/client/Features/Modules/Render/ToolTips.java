package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(
        name = "ToolTips",
        category = Module.Category.VISUALS,
        description = "Renders custom tooltips"
)
public class ToolTips extends Module {
    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> shulkers = null;
    public static Setting<Boolean> enableShulkers = null;
    public ToolTips() {
        enabled = this.isEnabled;
        shulkers = new Setting<>("Shulkers", false, this).setMode(Setting.Mode.SUB);
        enableShulkers = new Setting<>("Shulkers Tooltips", true, this).setVisibility(v-> shulkers.getValue());
    }
}
