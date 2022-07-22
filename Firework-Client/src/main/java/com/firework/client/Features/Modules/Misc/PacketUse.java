package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(name = "PacketUse",category = Module.Category.MISCELLANEOUS)
public class PacketUse extends Module {
    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> food = null;
    public static Setting<Boolean> potion = null;
    public static Setting<Boolean> all = null;
    public PacketUse(){
        enabled = this.isEnabled;
        all =  new Setting<>("All", true, this);
        food =  new Setting<>("Food", true, this).setVisibility(v-> all.getValue(false));
        potion =  new Setting<>("Potion", true, this).setVisibility(v-> all.getValue(false));
    }
}
