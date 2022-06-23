package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Enviroments extends Module {

    public Setting<Enum> page = new Setting<>("Page", pages.Time, this, pages.values());
    public enum pages{
        Time, Sky
    }

    public Setting<worlds> world = new Setting<>("World", worlds.OverWorld, this, worlds.values()).setVisibility(page,pages.Sky);
    public enum worlds{
        OverWorld, Hell
    }
    public Setting<Double> time = new Setting<>("Time", (double) 0, this, 0, 23000).setVisibility(page,pages.Time);

    public Setting<HSLColor> mainWorld = new Setting<>("Color", new HSLColor(1, 54, 43), this).setVisibility(world,worlds.OverWorld);
    public Setting<HSLColor> hellWorld = new Setting<>("Color", new HSLColor(1, 54, 43), this).setVisibility(world,worlds.Hell);


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
        if(!mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell")){
        event.setRed(mainWorld.getValue().toRGB().getRed() / 255f);
        event.setGreen(mainWorld.getValue().toRGB().getGreen() / 255f);
        event.setBlue(mainWorld.getValue().toRGB().getBlue() / 255f);}else{
            event.setRed(hellWorld.getValue().toRGB().getRed() / 255f);
            event.setGreen(hellWorld.getValue().toRGB().getGreen() / 255f);
            event.setBlue(hellWorld.getValue().toRGB().getBlue() / 255f);
        }
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }


}

