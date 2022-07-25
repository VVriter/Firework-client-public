package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "TickShift",category = Module.Category.MISCELLANEOUS)
public class TickShift extends Module {

    Timer timer = new Timer();
    Timer inhibitorTimer = new Timer();

    float defoultTickLeght;
    float speedTo;
    float currentTickLeght;

    public Setting<Boolean> disable = new Setting<>("AutoDisable", true, this);
    public Setting<Double> ticks = new Setting<>("Ticks", (double) 20, this, 1, 50);
    public Setting<Double> timerms = new Setting<>("Time", (double) 1, this, -0.3, 5);

 /*   public Setting<Boolean> inhibitor = new Setting<>("Inhibitor", false, this);
    public Setting<Double> startSpeed = new Setting<>("StartSpeed", (double) 250, this, 1, 1000).setVisibility(inhibitor, true);
    public Setting<Double> endSpeed = new Setting<>("EndSpeed", (double) 250, this, 1, 1000).setVisibility(inhibitor, true); */


    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
        inhibitorTimer.reset();
        defoultTickLeght = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        speedTo = ticks.getValue().floatValue();
        currentTickLeght = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();

        if (!(mc.player.moveForward > 0) && !(mc.player.moveVertical > 0)) {
            //If player stands
            timer.reset();
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defoultTickLeght);

        } else {
            if (!timer.hasPassedMs(timerms.getValue()*1000)) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defoultTickLeght - ticks.getValue().floatValue());
            } else {
                if (disable.getValue()) {onDisable();}
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defoultTickLeght);
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        timer.reset();
        inhibitorTimer.reset();
        ((ITimer)((IMinecraft)mc).getTimer()).setTickLength(defoultTickLeght);
    }
}
