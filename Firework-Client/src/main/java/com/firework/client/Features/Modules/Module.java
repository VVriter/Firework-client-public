package com.firework.client.Features.Modules;

import com.firework.client.Features.Modules.Client.Logger;
import com.firework.client.Features.Modules.Client.TestNotifications;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class Module extends Info{

    public static Minecraft mc = Minecraft.getMinecraft();
    public static EntityPlayerSP mcp = Minecraft.getMinecraft().player;

    public Category category;

    private int updateTimer = 0;
    public int delay = 20;
    public boolean existCheck;

    public String subCategory;

    public Setting<Boolean> isEnabled = new Setting<>("isEnabled", false, this).setVisibility(v-> false);
    public Setting<Boolean> isOpened = new Setting<>("isOpened", false, this).setVisibility(v-> false);
    public Setting<Integer> key = new Setting<>("Key", Keyboard.KEY_NONE, this);
    public Module(String name, Category category) {
        super(name);
        this.name = name;
        this.category = category;
    }

    public Module(){
        super(null);
        if (getClass().isAnnotationPresent(ModuleManifest.class)) {
            ModuleManifest args = getClass().getAnnotation(ModuleManifest.class);
            this.name = args.name();
            this.category = args.category();
            this.subCategory = args.subCategory();
        }
    }

    public void onEnable() {
        isEnabled.setValue(true);
        onToggle();
        MinecraftForge.EVENT_BUS.register(this);
        if(Logger.enabled.getValue() && Logger.onModuleEnable.getValue()){
            Logger.log(this);
        }

        TestNotifications.notificate(this.getName()+" is enabled!","1");
    }
    public void onDisable() {
        isEnabled.setValue(false);
        onToggle();
        MinecraftForge.EVENT_BUS.unregister(this);
        if(Logger.enabled.getValue() && Logger.onModuleDisable.getValue()){
            Logger.log(this);
        }

        TestNotifications.notificate(this.getName()+" is disabled","");
    }

    public void onToggle(){}

    public void toggle() {
        if(isEnabled.getValue()){
            onDisable();
        }else{
            onEnable();
        }
    }

    public void disable(){
        MinecraftForge.EVENT_BUS.unregister(this);
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
        CHAT, COMBAT, MOVEMENT, RENDER, MISC, WORLD, CLIENT
    }
}
