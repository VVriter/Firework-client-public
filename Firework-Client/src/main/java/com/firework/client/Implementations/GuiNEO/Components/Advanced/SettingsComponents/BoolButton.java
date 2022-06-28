package com.firework.client.Implementations.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.GuiNEO.GuiInfo.fillColorB;
import static com.firework.client.Implementations.GuiNEO.GuiInfo.outlineColorA;

public class BoolButton extends Button {

    public int activeColor;

    public BoolButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        activeColor = (boolean) setting.getValue() ? RainbowUtil.astolfoColors(100, 100) : Color.white.getRGB();

        int checkMarkWidth = 12;
        RenderUtils2D.drawCheckMarkV3(new Rectangle(x + width - checkMarkWidth - 2, y + 2, checkMarkWidth, 6), (Boolean) setting.getValue());
        textManager.drawString(setting.name, x+3, y+1,
                activeColor,false);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        setting.setValue(!(boolean)setting.getValue());
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }
}
