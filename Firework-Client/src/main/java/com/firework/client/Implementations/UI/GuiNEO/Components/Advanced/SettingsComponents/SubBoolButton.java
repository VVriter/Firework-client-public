package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;

import java.awt.*;

import static com.firework.client.Firework.textManager;

public class SubBoolButton extends Button {

    public int activeColor;

    public SubBoolButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        GuiInfo.drawBaseButton(this, GuiInfo.fillColorB, GuiInfo.outlineColorA);

        activeColor = (boolean) setting.getValue() ? RainbowUtil.astolfoColors(100, 100) : Color.white.getRGB();


        textManager.drawString(setting.name, x+3, y+1,
                activeColor,false);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        setting.setValue(!(boolean) setting.getValue());
    }
}
