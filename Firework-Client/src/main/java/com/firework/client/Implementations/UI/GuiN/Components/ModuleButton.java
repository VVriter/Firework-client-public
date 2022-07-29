package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.Components.ColorPicker.ColorPicker;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.firework.client.Firework.customFontManager;
import static com.firework.client.Firework.textManager;

public class ModuleButton extends Button{
    public ArrayList<Component> components = new ArrayList<>();
    public Module module;
    public Frame frame;

    public ModuleButton(Module module, Frame frame) {
        super(null, frame);
        this.module = module;
        this.frame = frame;
        for(Setting setting : Firework.settingManager.modulesSettings(module)){
            if(setting.mode == Setting.Mode.NUMBER)
                components.add(new SliderButton(setting, frame));
            if(setting.mode == Setting.Mode.BOOL)
                components.add(new BoolButton(setting, frame));
            if(setting.mode == Setting.Mode.MODE)
                components.add(new ModeButton(setting, frame));
            if(setting.mode == Setting.Mode.COLOR)
                components.add(new ColorPicker(setting, frame));
            if(setting.mode == Setting.Mode.SUB)
                components.add(new SubButton(setting, frame));
            if(setting.mode == Setting.Mode.KEY)
                components.add(new KeyButton(setting, frame));
        }
    }

    @Override
    public void setOffsets(final double minY){
        super.setOffsets(minY);
        if(module.isOpened.getValue()) {
            AtomicReference<Double> y = new AtomicReference<>(this.y + this.height);
            components.stream().filter(component -> component instanceof Button).forEach(component -> {
                if(((Button) component).setting.isVisible()) {
                    component.y = y.get();
                    y.updateAndGet(v -> v + component.getHeight());
                }
            });
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        int textWidth = textManager.getStringWidth(module.name);

        GuiInfo.drawButtonBase(this);
        if(module.isEnabled.getValue())
            RenderUtils2D.drawRectangle(new Rectangle(x + 1, y + 1, 1, height - 2), Firework.colorManager.getJuliet());
        customFontManager.drawString(module.name, x + (width-textWidth)/2, (float) (y+1),
                module.isEnabled.getValue() ? Firework.colorManager.getJuliet().getRGB() : Color.white.getRGB());

        if(module.isOpened.getValue()) {
            components.stream().filter(component -> component instanceof Button && ((Button) component).setting.isVisible()).forEach(component -> {
                component.draw(mouseX, mouseY);
            });
        }
    }

    @Override
    public int getHeight(){
        super.getHeight();
        AtomicInteger height = new AtomicInteger(this.height);
        if(module.isOpened.getValue()) {
            components.stream().filter(component -> component instanceof Button).forEach(component -> {
                if(((Button) component).setting.isVisible())
                    height.addAndGet(component.getHeight());
            });
        }
        return height.get();
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        components.forEach(component -> component.setX(x));
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 0)
            module.toggle();
        else if(state == 1)
            module.isOpened.setValue(!module.isOpened.getValue());
    }

    @Override
    public void onKeyTyped(final int keyCode) {
        super.onKeyTyped(keyCode);
        components.stream().filter(component -> component instanceof Button).forEach(component -> ((Button) component).onKeyTyped(keyCode));
    }
}
