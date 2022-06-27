package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(name = "Reach",category = Module.Category.MISC)
public class Reach extends Module {

    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> override = null;
    public static Setting<Double> reach = null;
    public static Setting<Double> add = null;

    public Reach(){
        enabled = this.isEnabled;
        override = new Setting<>("override", true, this);
        reach = new Setting<>("Reach", (double)3, this, 1, 15).setVisibility(override,false);
        add = new Setting<>("Add", (double)3, this, 1, 100).setVisibility(override,true);
    }
}
