package com.firework.client.Features.Modules;

import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import scala.Int;

import static com.firework.client.Firework.*;

public class Module {

    public Minecraft mc = Minecraft.getMinecraft();

    public String name;
    public Category category;

    private int updateTimer = 0;
    public int delay = 20;
    public boolean isNonCycle = false;
    public boolean existCheck;

    public Setting<Boolean> isEnabled = new Setting<>("isEnabled", false, this);
    public Setting<Boolean> isOpened = new Setting<>("isOpened", false, this);
    public Setting<Integer> key = new Setting<>("Key", Keyboard.KEY_NONE, this);

    public Module(String name, Category category, Boolean isNonCycle) {
        this.name = name;
        this.category = category;
        this.isNonCycle = isNonCycle;

        isEnabled.hidden = true;
        isOpened.hidden = true;
    }

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.isNonCycle = isNonCycle;

        isEnabled.hidden = true;
        isOpened.hidden = true;
    }

    public void onEnable() {
        isEnabled.setValue(true);
    }
    public void onDisable() {
        isEnabled.setValue(false);
    }

    public void onToggle() {
        isEnabled.setValue(!isEnabled.getValue());
    }

    //onUpdate
    //OnTick
    public void tryToExecute() {
        if(existCheck)
            if(mc.player == null | mc.world == null) return;

        if(isNonCycle) {
            //if NOT CYCLE
            isEnabled.setValue(!isEnabled.getValue());
        }
        //if CYCLE
        if(updateTimer != delay) {
            updateTimer++;
        } else {
            updateTimer = 0;
        }
    }

    public enum Category{
        COMBAT, MOVEMENT, RENDER, WORLD, CLIENT
    }
}
