package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonOffset;

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

    public static Setting<Boolean> breakblock = null;

    public NoRender(){
        super("NoRender",Category.RENDER);
        enabled = this.isEnabled;


        antiFog = new Setting<>("AntiFog", true, this).setVisibility(v-> page.getValue(pages.Misc));
        skylight = new Setting<>("Skylight", true, this).setVisibility(v-> page.getValue(pages.Misc));
        totemPops = new Setting<>("TotemPops", true, this).setVisibility(v-> page.getValue(pages.Misc));
        hurtcam = new Setting<>("Hurtcam", true, this).setVisibility(v-> page.getValue(pages.Misc));
        weather = new Setting<>("Weather", true, this).setVisibility(v-> page.getValue(pages.Misc));
        hands = new Setting<>("Hands", true, this).setVisibility(v-> page.getValue(pages.Misc));
        fov = new Setting<>("Fov", true, this).setVisibility(v-> page.getValue(pages.Misc));
        blockBreak = new Setting<>("BlockBreak", true, this).setVisibility(v-> page.getValue(pages.Misc));
        enchatntTable = new Setting<>("EnchantTable", true, this).setVisibility(v-> page.getValue(pages.Misc));
        viewBobbing =  new Setting<>("ViewBobbing", true, this).setVisibility(v-> page.getValue(pages.Misc));
        blockoverlay =  new Setting<>("BlockOverlay", true, this).setVisibility(v-> page.getValue(pages.Misc));
        mob = new Setting<>("Mob", true, this).setVisibility(v-> page.getValue(pages.Misc));
        xp = new Setting<>("Xp", true, this).setVisibility(v-> page.getValue(pages.Misc));
        explosions = new Setting<>("Explosions", true, this).setVisibility(v-> page.getValue(pages.Misc));
        breakblock = new Setting<>("BreakBlock", false, this).setVisibility(v-> page.getValue(pages.Misc));


        breakingParticles = new Setting<>("BreakingParticles", true, this).setVisibility(v-> page.getValue(pages.Particles));

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
    public void onRender3d(RenderWorldLastEvent e) {
        if (blockBreak.getValue()) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0f, -1100000.0f);
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
