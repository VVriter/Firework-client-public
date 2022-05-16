package com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.Gui.GuiInfo.fillColorB;
import static com.firework.client.Implementations.Gui.GuiInfo.outlineColorA;

public class ModeButton extends Button {

    public Setting setting;

    public ModeButton(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(setting.name + ":" + setting.getValue(), x+3, y+1,
                Color.WHITE.getRGB(),false);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);

        ++setting.index;
        if (setting.index > setting.list.size()-1) {
            setting.index = 0;
        }
        setting.setValue(setting.list.get(setting.index));
    }
}
