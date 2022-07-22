package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Math.Inhibitator;

@ModuleManifest(name = "TestInhibitator",category = Module.Category.CLIENT)
public class TestInhibitator extends Module {
    public Setting<Double> setting = new Setting<>("Setting", (double)1, this, 0, 100);


    @Override
    public void onTick() {
        super.onTick();
        Inhibitator.doInhibitation(setting,50,100,1);
    }

    @Override
    public void onToggle() {
        super.onToggle();
        Inhibitator.timer.reset();
    }

}
