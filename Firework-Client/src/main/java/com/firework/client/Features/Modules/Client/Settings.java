package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;

@ModuleManifest(name = "Settings", category = Module.Category.CLIENT)
public class Settings extends Module{

    public static Setting<modes> mode;
    public enum modes{
        Gradient, Single
    }
    public static Setting<HSLColor> Romeo;
    public static Setting<HSLColor> Juliet;

    public static Setting<HSLColor> Color;

    public Settings(){
        mode = new Setting<>("Mode", modes.Gradient, this);
        Romeo = new Setting<>("Romeo", new HSLColor(271, 72, 61), this).setVisibility(v-> mode.getValue(modes.Gradient));
        Juliet = new Setting<>("Juliet", new HSLColor(321, 72, 61), this).setVisibility(v-> mode.getValue(modes.Gradient));

        Color = new Setting<>("Color", new HSLColor(271, 72, 61), this).setVisibility(v-> mode.getValue(modes.Single));
    }
}
