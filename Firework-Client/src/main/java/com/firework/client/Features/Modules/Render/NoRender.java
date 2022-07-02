package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Client.Test;
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




    public Setting<Enum> page = new Setting<>("Page", pages.Misc, this, pages.values());
    public enum pages{
        Misc, Particles
    }


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
    public static Setting<Boolean> viewBobbing = null;
    public static Setting<Boolean> blockoverlay = null;
    public static Setting<Boolean> mob = null;
    public static Setting<Boolean> xp = null;
    public static Setting<Boolean> explosions = null;


    public static Setting<Boolean> breakingParticles = null;

    public NoRender(){
        super("NoRender",Category.RENDER);
        enabled = this.isEnabled;


        antiFog = new Setting<>("AntiFog", true, this).setVisibility(page,pages.Misc);
        skylight = new Setting<>("Skylight", true, this).setVisibility(page,pages.Misc);
        totemPops = new Setting<>("TotemPops", true, this).setVisibility(page,pages.Misc);
        hurtcam = new Setting<>("Hurtcam", true, this).setVisibility(page,pages.Misc);
        weather = new Setting<>("Weather", true, this).setVisibility(page,pages.Misc);
        hands = new Setting<>("Hands", true, this).setVisibility(page,pages.Misc);
        fov = new Setting<>("Fov", true, this).setVisibility(page,pages.Misc);
        blockBreak = new Setting<>("BlockBreak", true, this).setVisibility(page,pages.Misc);
        enchatntTable = new Setting<>("EnchantTable", true, this).setVisibility(page,pages.Misc);
        viewBobbing =  new Setting<>("ViewBobbing", true, this).setVisibility(page,pages.Misc);
        blockoverlay =  new Setting<>("BlockOverlay", true, this).setVisibility(page,pages.Misc);
        mob = new Setting<>("Mob", true, this).setVisibility(page,pages.Misc);
        xp = new Setting<>("Xp", true, this).setVisibility(page,pages.Misc);
        explosions = new Setting<>("Explosions", true, this).setVisibility(page,pages.Misc);


        breakingParticles = new Setting<>("BreakingParticles", true, this).setVisibility(page,pages.Particles);

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
                (packet instanceof SPacketExplosion && explosions.getValue()))
        {event.setCanceled(true);}
    }

    public void onDisable(){
        super.onDisable();
        mc.gameSettings.viewBobbing = true;
    }
}
