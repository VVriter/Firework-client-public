package com.firework.client.Features.Modules;

import com.firework.client.Implementations.Settings.Setting;

public class Module {

    public String name;
    public Category category;

    private int updateTimer = 0;
    public int delay = 20;
    public Boolean isNonCycle = true;

    public Setting<Boolean> isEnabled = new Setting<>("isEnabled", false, this);

    public Module(String name, Category category, Boolean isNonCycle) {
        this.name = name;
        this.category = category;
        this.isNonCycle = isNonCycle;
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

    public void execute() {}
    public void tryToExecute() {
        if(!isEnabled.getValue()) {
            return;
        }
        if(isNonCycle) {
            //if NOT CYCLE
            execute();
            isEnabled.setValue(!isEnabled.getValue());
            return;
        }
        //if CYCLE
        if(updateTimer != delay) {
            updateTimer++;
        } else {
            execute();
            updateTimer = 0;
        }
    }

    public enum Category{
        COMBAT, MOVEMENT, RENDER, WORLD, CLIENT
    }
}
