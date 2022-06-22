package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;


@ModuleManifest(name = "AutoRemount", category = Module.Category.WORLD)
public class AutoRemount extends Module {
    public Setting<Boolean> Bypass  = new Setting<>("Bypass", true, this);
    public Setting<Boolean> boat  = new Setting<>("boat", true, this);
    public Setting<Boolean> Minecart  = new Setting<>("Minecart", true, this);
    public Setting<Boolean> horse  = new Setting<>("horse", true, this);
    public Setting<Boolean> skeletonHorse  = new Setting<>("skeletonHorse", true, this);
    public Setting<Boolean> donkey  = new Setting<>("donkey", true, this);
    public Setting<Boolean> mule   = new Setting<>("mule", true, this);
    public Setting<Boolean> pig   = new Setting<>("pig ", true, this);
    public Setting<Boolean> llama   = new Setting<>("llama", true, this);


    public  Setting<Double> range  = new Setting<>("range", 2d, this, 0, 10);




}
