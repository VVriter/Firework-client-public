package com.firework.client.Features.Modules;

import com.firework.client.Features.Modules.Client.Logger;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import com.mysql.cj.jdbc.exceptions.NotUpdatable;
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
        isEnabled.setValue(true);
        onToggle();
        Firework.eventBus.register(this);
        Notifications.notificate(this.getName()+"toggeled on!","");
    }


    public void onDisable() {
        isEnabled.setValue(false);
        onToggle();
        Firework.eventBus.unregister(this);
        Notifications.notificate(this.getName()+"toggled off!","");
    }

    public void onEnableLog() {
        onEnable();
        if(Logger.enabled.getValue() && Logger.onModuleEnable.getValue()){
            Logger.log(this);
        }
    }

    public void onDisableLog() {
        onDisable();
        if(Logger.enabled.getValue() && Logger.onModuleDisable.getValue()){
            Logger.log(this);
        }
        if (Notifications.enabled.getValue()) {
            Notifications.notificate(this.getName()+" is disabled","");
        }
    }

    public void onToggle(){}

    public void toggle() {
        if(isEnabled.getValue()){
            onDisable();
        }else{
            onEnable();
        }
    }

    public void toggleLog() {
        if(isEnabled.getValue()){
            onDisableLog();
        }else{
            onEnableLog();
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
