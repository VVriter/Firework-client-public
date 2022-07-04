package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;

import java.awt.*;

import static com.firework.client.Firework.textManager;

public class ModeButton extends Button {

    public ModeButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        int textWidth = textManager.getStringWidth(setting.getValue().toString());

        GuiInfo.drawBaseButton(this, GuiInfo.fillColorB, GuiInfo.outlineColorA);

        textManager.drawString(setting.name, x+3, y+1,
                Color.WHITE.getRGB(),false);

        textManager.drawString(setting.getValue().toString(), x+width-2-textWidth, y+1,
                RainbowUtil.astolfoColors(100, 100),false);
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
