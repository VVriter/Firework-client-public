package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(
        name = "CameraTweaker",
        category = Module.Category.MISCELLANEOUS,
        description = "NoClip for camera"
)
public class CameraClip extends Module {

    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> extend = null;
    public static Setting<Double> valX = null;

    public CameraClip(){
        enabled = this.isEnabled;
        extend = new Setting<>("Custom", true, this);
        valX =  new Setting<>("Distance", (double)10, this, 0, 50).setVisibility(v-> extend.getValue(true));
    }
}
