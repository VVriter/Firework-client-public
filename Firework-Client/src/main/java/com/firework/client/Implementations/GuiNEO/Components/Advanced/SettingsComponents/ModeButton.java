package com.firework.client.Implementations.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.GuiNEO.GuiInfo.fillColorB;
import static com.firework.client.Implementations.GuiNEO.GuiInfo.outlineColorA;

public class ModeButton extends Button {

    public ModeButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(setting.getValue().toString());

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(setting.name, x+3, y+1,
                Color.WHITE.getRGB(),false);

        textManager.drawString(setting.getValue().toString(), x+width-2-textWidth, y+1,
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
