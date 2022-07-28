package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.TraceEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "NoMiningTrace",
        category = Module.Category.MISCELLANEOUS
)
public class NoMiningTrace extends Module {
    @Subscribe
    public Listener<TraceEvent> event = new Listener<>(e->{
        e.setCancelled(true);
    });
}
