package com.firework.client.Implementations.Settings;

import com.firework.client.Firework;
import com.firework.client.Features.Modules.Module;

import java.awt.*;
import java.util.List;

public class Setting<T> {

    private T value;
    public String name;
    public Module module;

    public List<String> list;
    public int index = 0;

    public Mode mode;

    public double min;
    public double max;

    public boolean hidden = false;
    public boolean opened = false;

    public Setting(String name, T value, Module module){
        this.name = name;
        this.value = value;
        this.module = module;

        if(value instanceof Boolean)
            this.mode = Mode.BOOL;

        if(value instanceof Integer)
            this.mode = Mode.KEY;

        if(value instanceof Color)
            this.mode = Mode.COLOR;

        Firework.settingManager.settings.add(this);
    }

    public Setting(String name, T value, Module module, double min, double max){
        this.name = name;
        this.value = value;
        this.module = module;
        this.min = min;
        this.max = max;

        this.mode = Mode.NUMBER;

        Firework.settingManager.settings.add(this);
    }

    public Setting(String name, T value, Module module, List<String> list){
        this.name = name;
        this.value = value;
        this.module = module;
        this.min = min;
        this.max = max;

        this.list = list;

        this.mode = Mode.MODE;

        Firework.settingManager.settings.add(this);
    }

    public void setValue(T newValue){
        this.value = newValue;
        Firework.settingManager.updateSettingsByName(this);
    }

    public T getValue(){
        return value;
    }

    public enum Mode{
        BOOL, NUMBER, MODE, KEY, COLOR
    }
}
