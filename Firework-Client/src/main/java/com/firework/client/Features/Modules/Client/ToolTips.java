package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;

@ModuleManifest(
        name = "ToolTips",
        category = Module.Category.CLIENT
)
public class ToolTips extends Module {
    public static Setting<Boolean> enabled = null;
    public static Setting<Double> scaling = null;
    public static Setting<HSLColor> color = null;
    public static Setting<HSLColor> color2 = null;
    public ToolTips() {enabled = this.isEnabled;  scaling = new Setting<>("Opacity", 200d, this, 5, 255); color2 = new Setting<>("FillColor", new HSLColor(1, 54, 43), this); color = new Setting<>("Color", new HSLColor(1, 54, 43), this);}
}
