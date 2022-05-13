package com.firework.client.Modules;

import com.firework.client.Settings.Setting;

public class Module {

    public String name;
    public Category category;

    private int updateTimer = 0;
    public int delay = 20;

    public Setting<Boolean> isEnable = new Setting<>("isEnabled", false, this);

    public Module(String name, Category category){
        this.name = name;
        this.category = category;
    }

    public void OnEnable(){isEnable.setValue(true);}
    public void OnDisable(){isEnable.setValue(false);}

    public void onToggle(){isEnable.setValue(!isEnable.getValue());}

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
