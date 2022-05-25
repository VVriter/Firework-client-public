package com.firework.client.Features.Modules;

import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class Module {

    public Minecraft mc = Minecraft.getMinecraft();

    public String name;
    public Category category;

    private int updateTimer = 0;
    public int delay = 20;
    public boolean existCheck;

    public Setting<Boolean> isEnabled = new Setting<>("isEnabled", false, this);
    public Setting<Boolean> isOpened = new Setting<>("isOpened", false, this);
    public Setting<Integer> key = new Setting<>("Key", Keyboard.KEY_NONE, this);
    public Module(String name, Category category) {
        this.name = name;
        this.category = category;

        isEnabled.hidden = true;
        isOpened.hidden = true;
    }

    public void onEnable() {
        isEnabled.setValue(true);
        MinecraftForge.EVENT_BUS.register(this);
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }
    public void onDisable() {
        isEnabled.setValue(false);
        MinecraftForge.EVENT_BUS.unregister(this);
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
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

        //if CYCLE
        if(updateTimer != delay) {
            updateTimer++;
        } else {
            updateTimer = 0;
        }
    }

    public enum Category{
        CHAT, COMBAT, MOVEMENT, RENDER, MISC, WORLD, CLIENT
    }
}
