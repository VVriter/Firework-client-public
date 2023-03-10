package com.firework.client.Implementations.Settings;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Settings.SettingModifyValueEvent;
import com.firework.client.Implementations.Utill.Render.HSLColor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.firework.client.Firework.settingManager;

public class Setting<T> {

    public T value;
    public String name;
    public Module module;

    public List<Object> list;

    public Supplier<Boolean> visibility;

    public int index = 0;

    public Mode mode;

    public double min;
    public double max;

    public boolean opened = false;

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

        if(value instanceof Enum){
            this.list = Arrays.asList(Arrays.stream(((Enum) this.value).getClass().getEnumConstants()).toArray());
            this.mode = Mode.MODE;
        }

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

    public void setValue(T newValue){
        this.value = newValue;
        Firework.eventBus.post(new SettingModifyValueEvent(this));
    }

    public void setValueNoEvent(T newValue){
        this.value = newValue;
    }

    public void setEnumValueNoEvent(String value) {
        if(getEnumByString(value) != null) {
            this.value = (T) getEnumByString(value);
        }
    }

    public Enum getEnumByString(String value) {
        for (Enum e : ((Enum) this.value).getClass().getEnumConstants()) {
            if (e.name().equalsIgnoreCase(value)) {
                return e;
            }
        }
        return null;
    }

    public Setting<T> setMode(Mode mode){
        this.mode = mode;
        return this;
    }

    public Setting<T> setVisibility(Supplier<Boolean> visibility){
        this.visibility = visibility;
        return this;
    }

    public boolean isVisible(){
        if (this.visibility == null) {
            return true;
        }
        return this.visibility.get();
    }

    public T getValue(){
        return value;
    }

    public boolean getValue(T checkValue){
        return getValue() == checkValue;
    }

    public enum Mode{
        BOOL, NUMBER, MODE, KEY, COLOR, SUB
    }
}
