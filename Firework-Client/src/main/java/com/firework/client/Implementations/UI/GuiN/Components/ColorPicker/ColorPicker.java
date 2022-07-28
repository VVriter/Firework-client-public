package com.firework.client.Implementations.UI.GuiN.Components.ColorPicker;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.Components.Button;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;
import java.util.ArrayList;

public class ColorPicker extends Button {

    ArrayList<Component> components = new ArrayList<>();

    HueBar hueBar;
    public ColorPicker(Setting setting, Frame frame) {
        super(setting, frame);
        components.add(hueBar = new HueBar(setting, x, y + height, width, 10));
    }

    @Override
    public void draw() {
        super.draw();
        GuiInfo.drawButtonBase(this);
        RenderUtils2D.drawAlphaBarBase(new Rectangle(x + width - 9, y + 2, 6, 6));
        if(setting.opened){
            hueBar.y = this.y + 10 + 30;
            components.forEach(Component::draw);
        }
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        components.forEach(component -> component.setX(x));
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 1)
            setting.opened = !setting.opened;
        if(state == 0){
            components.stream()
                    .filter(component -> GuiInfo.isHoveringOnTheComponent(component, mouseX, mouseY))
                    .forEach(component -> component.init(mouseX, mouseY, state));
        }
    }

    @Override
    public int getHeight() {
        if(setting.opened)
            return super.getHeight() + 40;
        else
            return super.getHeight();
    }
}
