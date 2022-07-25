package com.firework.client.Features.Modules;

import com.firework.client.Features.Modules.Client.Logger;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class Module{

    public static Minecraft mc = Minecraft.getMinecraft();
    public static EntityPlayerSP mcp = Minecraft.getMinecraft().player;

    public Category category;

    private int updateTimer = 0;
    public int delay = 20;
    public boolean existCheck;

    public String name;

    public Setting<Boolean> isEnabled = new Setting<>("isEnabled", false, this).setVisibility(v-> false);
    public Setting<Boolean> isOpened = new Setting<>("isOpened", false, this).setVisibility(v-> false);
    public Setting<Integer> key = new Setting<>("Key", Keyboard.KEY_NONE, this);
    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Module(){
        if (getClass().isAnnotationPresent(ModuleManifest.class)) {
            ModuleManifest args = getClass().getAnnotation(ModuleManifest.class);
            this.name = args.name();
            this.category = args.category();
        }
    }

    public void onEnable() {
        if (!Notifications.enabled.getValue()) {
            SoundUtill.playSound(new ResourceLocation("firework/audio/pop.wav"));
        }
        isEnabled.setValue(true);
        onToggle();
        MinecraftForge.EVENT_BUS.register(this);
        Firework.eventBus.register(this);
        if(Logger.enabled.getValue() && Logger.onModuleEnable.getValue()){
            Logger.log(this);
        }
        if (Notifications.enabled.getValue()) {
            Notifications.notificate(this.getName()+" is enabled!","1");
        }
    }
    public void onDisable() {
        isEnabled.setValue(false);
        onToggle();
        MinecraftForge.EVENT_BUS.unregister(this);
        Firework.eventBus.unregister(this);
        if(Logger.enabled.getValue() && Logger.onModuleDisable.getValue()){
            Logger.log(this);
        }
        if (Notifications.enabled.getValue()) {
            Notifications.notificate(this.getName()+" is disabled","");
        }
    }

    public void onEnableNoLog() {
        isEnabled.setValue(true);
        onToggle();
        MinecraftForge.EVENT_BUS.register(this);
        Firework.eventBus.register(this);
    }

    public void onToggle(){}

    public void toggle() {
        if(isEnabled.getValue()){
            onDisable();
        }else{
            onEnable();
        }
    }
    public void onTick() {
        if(existCheck)
            if(mc.player == null | mc.world == null) return;

        if(updateTimer != delay) {
            updateTimer++;
        } else {
            updateTimer = 0;
            onUpdate();
        }
    }

    public void onUpdate(){}

    public String getName() {return name;}

    public boolean fullNullCheck() {
        return mc.player == null || mc.world == null;
    }
    public enum Category{
        CHAT,
        COMBAT,
        MOVEMENT,
        VISUALS,
        MISCELLANEOUS,
        WORLD,
        CLIENT
    }
}
