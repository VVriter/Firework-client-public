package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Movement.MoveEvent;
import net.minecraft.entity.MoverType;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "PacketFly",
        category = Module.Category.MOVEMENT,
        description = ""
)
public class PacketFly extends Module {
    @Subscribe
    public Listener<MoveEvent> ev = new Listener<>(e-> {
            e.setType(MoverType.PISTON);
    });
}
