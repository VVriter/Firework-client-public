package com.firework.client.Implementations.Settings;

import com.firework.client.Firework;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Utill.Render.HSLColor;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static com.firework.client.Firework.settingManager;

public class Setting<T> {

    private T value;
    public String name;
    public Module module;

    public List<Object> list;
    public int index = 0;

    public Mode mode;

    public double min;
    public double max;

    public boolean opened = false;

    public boolean hidden = false;

    public Setting targetSetting;
    public Object targetValue;

    public Setting(String name, T value, Module module){
        this.name = name;
        this.value = value;
        this.module = module;

        if(value instanceof Boolean)
            this.mode = Mode.BOOL;

        if(value instanceof Integer)
            this.mode = Mode.KEY;

        if(value instanceof HSLColor)
            this.mode = Mode.COLOR;

        settingManager.settings.add(this);
    }

    public Setting(String name, T value, Module module, double min, double max){
        this.name = name;
        this.value = value;
        this.module = module;
        this.min = min;
        this.max = max;

        this.mode = Mode.NUMBER;

        settingManager.settings.add(this);
    }

    public Setting(String name, T value, Module module, List<String> list){
        this.name = name;
        this.value = value;
        this.module = module;

        this.list = Arrays.asList(list.toArray());

        this.mode = Mode.MODE;

        settingManager.settings.add(this);
    }

    public Setting(String name, T value, Module module, Enum[] list){
        this.name = name;
        this.value = value;
        this.module = module;

        this.list = Arrays.asList(Arrays.stream(list).toArray());

        this.mode = Mode.MODE;

        settingManager.settings.add(this);
    }

    public void setValue(T newValue){
        this.value = newValue;
        settingManager.updateSettingsByName(this);
    }

    public Setting<T> setVisibility(boolean visibility){
        this.hidden = !visibility;
        settingManager.updateSettingsByName(this);
        return this;
    }

    public Setting<T> setVisibility(Setting setting, Object object){
        this.targetSetting = setting;
        this.targetValue = object;
        settingManager.updateSettingsByName(this);
        updateSettingVisibility();
        settingManager.updateSettingsByName(this);
        return this;
    }

    public void updateSettingVisibility(){
        if(targetSetting != null && targetValue != null) {
            this.hidden = targetSetting.getValue(targetValue) ? false : true;
            settingManager.updateSettingsByName(this);
        }
    }

    public T getValue(){
        return value;
    }

    public boolean getValue(T checkValue){
        if(getValue() == checkValue) return true;
        return false;
    }

    public enum Mode{
        BOOL, NUMBER, MODE, KEY, COLOR
    }
}
