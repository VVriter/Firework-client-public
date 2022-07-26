package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "XCarry",category = Module.Category.MISCELLANEOUS)
public class XCarry extends Module {

    @Subscribe
    public Listener<PacketEvent.Send> onPacket = new Listener<>(e -> {
        if(e.getPacket() instanceof CPacketCloseWindow){
            e.setCancelled(true);
        }
    });
}
