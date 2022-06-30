package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(name = "CameraTweaker",category = Module.Category.MISC)
public class CameraClip extends Module {

    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> extend = null;
    public static Setting<Double> valX = null;
    public static Setting<Double> valZ = null;

    public CameraClip(){
        enabled = this.isEnabled;
        extend = new Setting<>("Custom", true, this);
        valX =  new Setting<>("Distance", (double)10, this, 0, 50).setVisibility(extend,true);
    }
}
