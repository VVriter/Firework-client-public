package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;

@ModuleManifest(name = "TickShift",category = Module.Category.MISC)
public class TickShift extends Module {

    Timer timer = new Timer();
    float defoultTickLeght;

    public Setting<Double> ticks = new Setting<>("Ticks", (double)40, this, 1, 50);
    public Setting<Double> timerms = new Setting<>("Time", (double)1000, this, 1, 5000);

    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
        defoultTickLeght =  ((ITimer)((IMinecraft)mc).getTimer()).getTickLength();
    }
    @Override
    public void onTick() {
        super.onTick();
        if (!(mc.player.moveForward > 0) && !(mc.player.moveVertical > 0)) {
            //If player stands
            timer.reset();
           ((ITimer)((IMinecraft)mc).getTimer()).setTickLength(defoultTickLeght);
        } else {
            //Else
            if (!timer.hasPassedMs(timerms.getValue())) {
                ((ITimer)((IMinecraft)mc).getTimer()).setTickLength(ticks.getValue().floatValue());
            } else {
                ((ITimer)((IMinecraft)mc).getTimer()).setTickLength(defoultTickLeght);
            }
        }
    }


    @Override
    public void onDisable() {
        super.onDisable();
        timer.reset();
        ((ITimer)((IMinecraft)mc).getTimer()).setTickLength(defoultTickLeght);
    }

}
