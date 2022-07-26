package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;

@ModuleManifest(name = "BetterTabOverlay",category = Module.Category.VISUALS)
public class BetterTabOverlay extends Module {
    public static Setting<HSLColor> fillColorNickNames = null;
    public static Setting<HSLColor> fillColorBorder = null;
    public static Setting<Boolean> enabled = null;
    public BetterTabOverlay() {
        fillColorNickNames = new Setting<>("Color1", new HSLColor(1, 54, 43), this);
        fillColorBorder = new Setting<>("Color2", new HSLColor(1, 54, 43), this);
        enabled = this.isEnabled;
    }
}
