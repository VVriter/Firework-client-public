package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static com.firework.client.Firework.textManager;

public class KeyButton extends Button {

    public Color fillColor = GuiInfo.fillColorB;

    public KeyButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        String value = Keyboard.getKeyName((Integer) setting.getValue());

        int textWidth = textManager.getStringWidth(value);

        GuiInfo.drawBaseButton(this, fillColor, GuiInfo.outlineColorA);

        textManager.drawString(setting.name, x + 3, y + 1,
                Color.WHITE.getRGB(), false);

        textManager.drawString(value, x+width-2-textWidth, y + 1,
                RainbowUtil.astolfoColors(100, 100), false);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        fillColor = GuiInfo.fillColorA;
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }

    @Override
    public void onKeyTyped(int keyCode) {
        super.onKeyTyped(keyCode);
        if(Gui.keyIsDragging){
            if(Gui.activeKeyModule == setting.module.name) {
                Gui.keyIsDragging = false;
                fillColor = GuiInfo.fillColorB;
                setting.setValue(keyCode);
                Gui.activeKeyModule = "";
            }
        }
    }
}