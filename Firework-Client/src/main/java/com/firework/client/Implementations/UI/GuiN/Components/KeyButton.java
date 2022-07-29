package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Objects;

import static com.firework.client.Firework.customFontManager;
import static com.firework.client.Firework.textManager;

public class KeyButton extends Button{
    public KeyButton(Setting setting, Frame frame) {
        super(setting, frame);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        GuiInfo.drawButtonBase(this);

        boolean active = Objects.equals(GuiN.activeKey, this.setting);
        String value = Keyboard.getKeyName((int) setting.getValue());
        double textWidth = customFontManager.getWidth(value);

        textManager.drawString(setting.name, (float) (x + 3), (float) (y + 1),
                active ? Color.gray.getRGB() : Color.white.getRGB(),false);

        textManager.drawString(value, (float) (x+width-2-textWidth), (float) (y + 1),
                active ? Color.gray.getRGB() : Firework.colorManager.getJuliet().getRGB(), false);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(!Objects.equals(GuiN.activeKey, this.setting))
            GuiN.activeKey = this.setting;
        else
            GuiN.activeKey = null;
    }

    @Override
    public void onKeyTyped(final int keyCode) {
        super.onKeyTyped(keyCode);
        if(Objects.equals(GuiN.activeKey, this.setting)){
            this.setting.setValue(keyCode);
            GuiN.activeKey = null;
        }
    }
}
