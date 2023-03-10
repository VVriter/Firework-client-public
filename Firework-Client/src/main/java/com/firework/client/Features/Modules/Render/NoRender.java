package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Render.FireRendereEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "NoRender",
        category = Module.Category.VISUALS,
        description = "Prevents rendering shit"
)
public class NoRender extends Module {

    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> skySubBool = null;
    public static Setting<Boolean> cameraSubBool = null;
    public static Setting<Boolean> blocksSubBool = null;
    public static Setting<Boolean> entitiesSubBool = null;

    public static Setting<Boolean> guiSubBool = null;
    public static Setting<Boolean> antiFog = null;
    public static Setting<Boolean> skylight = null;
    public static Setting<Boolean> bosssOverlay = null;
    public static Setting<Boolean> itemFrame = null;
    public static Setting<Boolean> weather = null;
    public static Setting<Boolean> totemPops = null;
    public static Setting<Boolean> hurtcam = null;
    public static Setting<Boolean> potionEffects = null;
    public static Setting<Boolean> hands = null;
    public static Setting<Boolean> fov = null;

    public static Setting<Boolean> viewBobbing = null;
    public static Setting<Boolean> enchatntTable = null;
    public static Setting<Boolean> blockoverlay = null;
    public static Setting<Boolean> mob = null;
    public static Setting<Boolean> xp = null;
    public static Setting<Boolean> explosions = null;

    public static Setting<Boolean> defaultBackground = null;

    public static Setting<Boolean> armourSubBool = null;
    public static Setting<Boolean> helmet = null;
    public static Setting<Boolean> chestplate = null;
    public static Setting<Boolean> leggings = null;
    public static Setting<Boolean> boots = null;

    public NoRender(){
        enabled = this.isEnabled;

        skySubBool = new Setting<>("Sky", false, this).setMode(Setting.Mode.SUB);
        antiFog = new Setting<>("AntiFog", true, this).setVisibility(()-> skySubBool.getValue());
        skylight = new Setting<>("Skylight", true, this).setVisibility(()-> skySubBool.getValue());
        weather = new Setting<>("Weather", true, this).setVisibility(()-> skySubBool.getValue());

        cameraSubBool = new Setting<>("Camera", false, this).setMode(Setting.Mode.SUB);
        totemPops = new Setting<>("TotemPops", true, this).setVisibility(()-> cameraSubBool.getValue());
        hurtcam = new Setting<>("Hurtcam", true, this).setVisibility(()-> cameraSubBool.getValue());
        hands = new Setting<>("Hands", true, this).setVisibility(()-> cameraSubBool.getValue());
        fov = new Setting<>("Fov", true, this).setVisibility(()-> cameraSubBool.getValue());
        viewBobbing =  new Setting<>("ViewBobbing", true, this).setVisibility(()-> cameraSubBool.getValue());
        explosions = new Setting<>("Explosions", true, this).setVisibility(()-> cameraSubBool.getValue());
        potionEffects = new Setting<>("PotionEffects", true, this).setVisibility(()-> cameraSubBool.getValue());

        blocksSubBool = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
        enchatntTable = new Setting<>("EnchantTable", true, this).setVisibility(()-> blocksSubBool.getValue());
        blockoverlay =  new Setting<>("BlockOverlay", true, this).setVisibility(()-> blocksSubBool.getValue());

        entitiesSubBool = new Setting<>("Entities", false, this).setMode(Setting.Mode.SUB);
        mob = new Setting<>("Mob", true, this).setVisibility(()-> entitiesSubBool.getValue());
        xp = new Setting<>("Xp", true, this).setVisibility(()-> entitiesSubBool.getValue());
        itemFrame = new Setting<>("ItemFrame", false, this).setVisibility(()-> entitiesSubBool.getValue());

        guiSubBool = new Setting<>("Gui", false, this).setMode(Setting.Mode.SUB);
        defaultBackground = new Setting<>("DefaultBackground", true, this).setVisibility(()-> guiSubBool.getValue());
        bosssOverlay = new Setting<>("BossesOverlay", true, this).setVisibility(()-> guiSubBool.getValue());

        armourSubBool = new Setting<>("Armour",false,this).setMode(Setting.Mode.SUB);
        helmet = new Setting<>("Helmet",false,this).setVisibility(()-> armourSubBool.getValue());
        chestplate = new Setting<>("Chestplate",false,this).setVisibility(()-> armourSubBool.getValue());
        leggings = new Setting<>("Leggings",false,this).setVisibility(()-> armourSubBool.getValue());
        boots = new Setting<>("Boots",false,this).setVisibility(()-> armourSubBool.getValue());
    }


    @Override
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
    @Subscribe
    public Listener<PacketEvent.Receive> onRender = new Listener<>(event -> {
        Packet packet = event.getPacket();
        if ((packet instanceof SPacketSpawnMob && mob.getValue()) ||
                (packet instanceof SPacketSpawnExperienceOrb && xp.getValue()) ||
                (packet instanceof SPacketExplosion && explosions.getValue()))
        {event.setCancelled(true);}
    });

    @Subscribe
    public Listener<FireRendereEvent> evvv = new Listener<>(e-> {
        if (potionEffects.getValue()) {
            e.setCancelled(true);
        }
    });

    @Override
    public void onDisable(){
        super.onDisable();
        mc.gameSettings.viewBobbing = true;
    }
}
