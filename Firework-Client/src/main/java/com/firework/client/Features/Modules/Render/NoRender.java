package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender extends Module {
    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> antiFog = null;
    public static Setting<Boolean> skylight = null;
    public static Setting<Boolean> totemPops = null;
    public static Setting<Boolean> hurtcam = null;
    public static Setting<Boolean> weather = null;
    public static Setting<Boolean> hands = null;
    public static Setting<Boolean> fov = null;

    public static Setting<Boolean> blockBreak = null;

    public static Setting<Boolean> enchatntTable = null;

    public Setting<Boolean> viewBobbing = new Setting<>("ViewBobbing", true, this);
    public Setting<Boolean> blockoverlay = new Setting<>("BlockOverlay", true, this);

    public Setting<Boolean> mob = new Setting<>("Mob", true, this);
    public Setting<Boolean> xp = new Setting<>("Xp", true, this);
    public Setting<Boolean> explosions = new Setting<>("Explosions", true, this);
    public Setting<Boolean> particles = new Setting<>("Particles", true, this);

    public NoRender(){
        super("NoRender",Category.RENDER);
        enabled = this.isEnabled;
        antiFog = new Setting<>("AntiFog", true, this);
        skylight = new Setting<>("Skylight", true, this);
        totemPops = new Setting<>("TotemPops", true, this);
        hurtcam = new Setting<>("Hurtcam", true, this);
        weather = new Setting<>("Weather", true, this);
        hands = new Setting<>("Hands", true, this);
        fov = new Setting<>("Fov", true, this);
        blockBreak = new Setting<>("BlockBreak", true, this);
        enchatntTable = new Setting<>("EnchantTable", true, this);
    }

    public void onEnable(){
        super.onEnable();
        if(viewBobbing.getValue()){mc.gameSettings.viewBobbing = false;}
        if(!viewBobbing.getValue()){mc.gameSettings.viewBobbing = true;}
    }

    @SubscribeEvent
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {
        if(antiFog.getValue()){
        event.setDensity(0);
        event.setCanceled(true);}
    }

    @SubscribeEvent
    public void blockoverlay(RenderBlockOverlayEvent event){
        if(blockoverlay.getValue()){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event){
        Packet packet = event.getPacket();
        if ((packet instanceof SPacketSpawnMob && mob.getValue()) ||
                (packet instanceof SPacketSpawnExperienceOrb && xp.getValue()) ||
                (packet instanceof SPacketExplosion && explosions.getValue()) ||
                (packet instanceof SPacketParticles && particles.getValue()))
        {event.setCanceled(true);}
    }

    public void onDisable(){
        super.onDisable();
        mc.gameSettings.viewBobbing = true;
    }
}
