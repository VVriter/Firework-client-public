package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Enviroments extends Module {

    public Setting<Double> time = new Setting<>("Time", (double) 0, this, 0, 23000);
    public Setting<HSLColor> mainWorld = new Setting<>("Sky Color", new HSLColor(1, 54, 43), this);


    public Enviroments() {
        super("Enviroments", Category.RENDER);
    }

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


    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(mainWorld.getValue().toRGB().getRed() / 255f);
        event.setGreen(mainWorld.getValue().toRGB().getGreen() / 255f);
        event.setBlue(mainWorld.getValue().toRGB().getBlue() / 255f);
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }


}

