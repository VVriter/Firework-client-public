package com.firework.client.Features.Modules;

import com.firework.client.Features.Modules.Client.CommandLineLogger;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class Module extends Info{

    public Minecraft mc = Minecraft.getMinecraft();

    public Category category;

    private int updateTimer = 0;
    public int delay = 20;
    public boolean existCheck;

    public String subCategory;

    public Setting<Boolean> isEnabled = new Setting<>("isEnabled", false, this).setVisibility(false);
    public Setting<Boolean> isOpened = new Setting<>("isOpened", false, this).setVisibility(false);
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
        MinecraftForge.EVENT_BUS.register(this);
        if(CommandLineLogger.enabled.getValue() && CommandLineLogger.onModuleEnable.getValue()){
            CommandLineLogger.log("Module with the name "+this.name+" is enabled!");
        }
    }
    public void onDisable() {
        isEnabled.setValue(false);
        MinecraftForge.EVENT_BUS.unregister(this);
        if(CommandLineLogger.enabled.getValue() && CommandLineLogger.onModuleDisable.getValue()){
            CommandLineLogger.log("Module with the name "+this.name+" is disabled!");
        }
    }

    public void onToggle() {
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

    public enum Category{
        CHAT, COMBAT, MOVEMENT, RENDER, MISC, WORLD, CLIENT, HUD
    }
}
