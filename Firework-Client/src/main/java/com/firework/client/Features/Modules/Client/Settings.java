package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;

@ModuleManifest(name = "Settings", category = Module.Category.CLIENT)
public class Settings extends Module{

    public static Setting<HSLColor> Romeo;
    public static Setting<HSLColor> Juliet;

    public Settings(){
        Romeo = new Setting<>("Romeo", new HSLColor(271, 72, 61), this);
        Juliet = new Setting<>("Juliet", new HSLColor(321, 72, 61), this);
    }
}
