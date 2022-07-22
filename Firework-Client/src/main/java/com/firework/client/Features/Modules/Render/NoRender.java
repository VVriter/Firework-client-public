package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender extends Module {

    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> skySubBool = null;
    public static Setting<Boolean> cameraSubBool = null;
    public static Setting<Boolean> blocksSubBool = null;
    public static Setting<Boolean> entitiesSubBool = null;
    public static Setting<Boolean> antiFog = null;
    public static Setting<Boolean> skylight = null;

    public static Setting<Boolean> weather = null;
    public static Setting<Boolean> totemPops = null;
    public static Setting<Boolean> hurtcam = null;
    public static Setting<Boolean> hands = null;
    public static Setting<Boolean> fov = null;

    public static Setting<Boolean> viewBobbing = null;
    public static Setting<Boolean> enchatntTable = null;
    public static Setting<Boolean> blockoverlay = null;
    public static Setting<Boolean> mob = null;
    public static Setting<Boolean> xp = null;
    public static Setting<Boolean> explosions = null;



    public NoRender(){
        super("NoRender",Category.VISUALS);
        enabled = this.isEnabled;

        skySubBool = new Setting<>("Sky", false, this).setMode(Setting.Mode.SUB);
        antiFog = new Setting<>("AntiFog", true, this).setVisibility(v-> skySubBool.getValue());
        skylight = new Setting<>("Skylight", true, this).setVisibility(v-> skySubBool.getValue());
        weather = new Setting<>("Weather", true, this).setVisibility(v-> skySubBool.getValue());

        cameraSubBool = new Setting<>("Camera", false, this).setMode(Setting.Mode.SUB);
        totemPops = new Setting<>("TotemPops", true, this).setVisibility(v-> cameraSubBool.getValue());
        hurtcam = new Setting<>("Hurtcam", true, this).setVisibility(v-> cameraSubBool.getValue());
        hands = new Setting<>("Hands", true, this).setVisibility(v-> cameraSubBool.getValue());
        fov = new Setting<>("Fov", true, this).setVisibility(v-> cameraSubBool.getValue());
        viewBobbing =  new Setting<>("ViewBobbing", true, this).setVisibility(v-> cameraSubBool.getValue());
        explosions = new Setting<>("Explosions", true, this).setVisibility(v-> cameraSubBool.getValue());

        blocksSubBool = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
        enchatntTable = new Setting<>("EnchantTable", true, this).setVisibility(v-> blocksSubBool.getValue());
        blockoverlay =  new Setting<>("BlockOverlay", true, this).setVisibility(v-> blocksSubBool.getValue());

        entitiesSubBool = new Setting<>("Entities", false, this).setMode(Setting.Mode.SUB);
        mob = new Setting<>("Mob", true, this).setVisibility(v-> entitiesSubBool.getValue());
        xp = new Setting<>("Xp", true, this).setVisibility(v-> entitiesSubBool.getValue());

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
