package com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Gui.Gui;
import com.firework.client.Implementations.Gui.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.Gui.GuiInfo.*;

public class KeyButton extends Button {

    public Setting setting;

    public Color fillColor = fillColorB;

    public KeyButton(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);

        this.setting = setting;
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColor);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(setting.name + ":" + Keyboard.getKeyName((int) setting.getValue()), x + 3, y + 1,
                Color.WHITE.getRGB(), false);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        fillColor = fillColorA;
    }

    @Override
    public void onKeyTyped(int keyCode) {
        super.onKeyTyped(keyCode);
        if(Gui.keyIsDragging){
            Gui.keyIsDragging = false;
            fillColor = fillColorB;
            setting.setValue(keyCode);
        }
    }
}