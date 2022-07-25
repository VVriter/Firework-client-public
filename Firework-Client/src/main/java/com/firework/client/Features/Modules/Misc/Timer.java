package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "Timer",category = Module.Category.MISCELLANEOUS)
public class Timer extends Module {

    public Setting<Double> ticksTo = new Setting<>("TicksAdd", (double)25, this, 0, 50);
    public Setting<Boolean> onMove = new Setting<>("OnMove", true, this);


    float defaultTimerTicks;

    @Override
    public void onEnable() {
        super.onEnable();
        defaultTimerTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
    }


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        if (!onMove.getValue()) {
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks - ticksTo.getValue().floatValue());
        } else {
            if (mc.player.moveForward > 0) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks - ticksTo.getValue().floatValue());
            } else {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks);
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks);
    }
}
