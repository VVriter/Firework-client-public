package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.ShouldWalkOffEdgeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "SafeWalk",
        category = Module.Category.MOVEMENT,
        description = ""
)
public class SafeWalk extends Module {
    @Subscribe
    public Listener<ShouldWalkOffEdgeEvent> lol = new Listener<>(e-> {
        e.setCancelled(true);
    });
}
