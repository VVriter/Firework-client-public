package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "XCarry",category = Module.Category.MISC)
public class XCarry extends Module {
    @SubscribeEvent
    public void onPacket(PacketEvent.Send e){
        if(e.getPacket() instanceof CPacketCloseWindow){
            e.setCanceled(true);
        }
    }
}
