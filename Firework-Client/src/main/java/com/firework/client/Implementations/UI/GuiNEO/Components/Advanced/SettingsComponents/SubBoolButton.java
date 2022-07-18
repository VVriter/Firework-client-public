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

    public SubBoolButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        GuiInfo.drawBaseButtonGradient(this, new Color(RainbowUtil.astolfoColors(100, 100)), new Color(RainbowUtil.astolfoColors(50, 100)), GuiInfo.outlineColorA, false);
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), new Color(1, 1, 1, 24));

        textManager.drawString(setting.name, x+3, y+1,
                Color.white.getRGB(),false);

        String icon = (boolean)setting.getValue() ? "=" : "+";

        textManager.drawString(icon, x + width - textManager.getStringWidth(icon) - 2, y+1,
                Color.white.getRGB(),false);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        setting.setValue(!(boolean) setting.getValue());
    }
}
