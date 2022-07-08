package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.Interfaces.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.Interfaces.ITimer;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(name = "Timer",category = Module.Category.MISC)
public class Timer extends Module {

    public Setting<Double> ticksTo = new Setting<>("TicksAdd", (double)25, this, 0, 50);

    float defaultTimerTicks;

    @Override
    public void onEnable() {
        super.onEnable();
        defaultTimerTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
    }


    @Override
    public void onTick() {
        super.onTick();
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks - ticksTo.getValue().floatValue());
    }


    @Override
    public void onDisable() {
        super.onDisable();
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks);
    }
}
