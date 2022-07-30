package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "PortalGodMod",
        category = Module.Category.MISCELLANEOUS,
        description = "Portal God mode"
)
public class PortalGodMode extends Module {
    @Subscribe
    public Listener<PacketEvent.Send> listener = new Listener<>(e->{
        if (e.getPacket() instanceof CPacketConfirmTeleport) {
            e.setCancelled(true);
        }
    });
}
