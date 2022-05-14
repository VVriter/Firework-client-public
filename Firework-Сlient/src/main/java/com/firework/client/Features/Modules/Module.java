package com.firework.client.Features.Modules;

import com.firework.client.Implementations.Settings.Setting;

public class Module {

    public String name;
    public Category category;

    private int updateTimer = 0;
    public int delay = 20;

    public Setting<Boolean> isEnabled = new Setting<>("isEnabled", false, this);

    public Module(String name, Category category){
        this.name = name;
        this.category = category;
    }

    public void onEnable(){
        isEnabled.setValue(true);}
    public void onDisable(){
        isEnabled.setValue(false);}

    public void onToggle(){
        isEnabled.setValue(!isEnabled.getValue());}

    public void onUpdate(){}
    public void onTick(){
        if(updateTimer != delay){
            updateTimer++;
        }else{
            onUpdate();
            updateTimer = 0;
        }
    }

    public enum Category{
        COMBAT, MOVEMENT, RENDER, WORLD, CLIENT
    }
}
