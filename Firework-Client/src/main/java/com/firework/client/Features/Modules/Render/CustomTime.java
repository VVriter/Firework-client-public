package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomTime extends Module {

    public Setting<Double> time = new Setting<>("Time", (double)0, this, 0, 23000);



    public CustomTime(){super("Enviroments",Category.RENDER);}
    @Override
    public void onTick() {


        mc.world.setWorldTime(time.getValue().longValue());
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCanceled(true);
        }
    }

}
