package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketCustomPayload;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "NoForge",
        category = Module.Category.CLIENT,
        description = "Sends fake info to server u joined to prevent ban"
)
public class NoForge extends Module {
    @Subscribe
    public Listener<PacketEvent.Send> onRender = new Listener<>(event -> {
        if (!mc.isIntegratedServerRunning()) {
            if (event.getPacket().getClass().getName().equals("net.minecraftforge.fml.common.network.internal.FMLProxyPacket")) {
                event.setCancelled(true);
            } else if (event.getPacket() instanceof CPacketCustomPayload) {
                if (((CPacketCustomPayload) event.getPacket()).getChannelName().equalsIgnoreCase("MC|Brand")) {
                    ((ICPacketCustomPayload) event.getPacket()).setData(new PacketBuffer(Unpooled.buffer()).writeString("vanilla"));
                }
            }
        }
    });
}
