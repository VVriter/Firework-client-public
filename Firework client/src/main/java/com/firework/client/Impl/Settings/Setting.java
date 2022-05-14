package com.firework.client.Impl.Settings;

import com.firework.client.Firework;
import com.firework.client.Features.Modules.Module;

import java.util.List;

public class Setting<T> {

    private T value;
    public String name;
    public Module module;

    public Mode mode;

    public double min;
    public double max;

    public Setting(String name, T value, Module module){
        this.name = name;
        this.value = value;
        this.module = module;

        this.mode = Mode.BOOL;

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

    public Setting(String name, T value, Module module, List<String> list, double min, double max){
        this.name = name;
        this.value = value;
        this.module = module;
        this.min = min;
        this.max = max;

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
        BOOL, NUMBER, MODE
    }
}
