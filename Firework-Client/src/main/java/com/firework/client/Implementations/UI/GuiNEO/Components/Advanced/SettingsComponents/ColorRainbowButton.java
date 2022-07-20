package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Managers.Updater.Updater;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.*;

import java.awt.*;
import java.util.ArrayList;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.fillColorA;
import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.outlineColorA;
import static com.firework.client.Implementations.UI.GuiNEO.GuiValueStorage.values;

public class ColorRainbowButton extends Button {
    public Color activeColor;
    private int tmpIndex;

    public ColorRainbowButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);

        this.offset = setting.opened ? 10 : 0; this.originOffset = this.offset;
        this.height = setting.opened ? 10 : 0; this.originHeight = this.height;

        tmpIndex = localIndex - 1;
        if(values.get(tmpIndex) == null){
            values.set(tmpIndex, new ArrayList());
            values.get(tmpIndex).add(0, false);
        }

    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(setting.opened != true) return;
        super.draw(mouseX, mouseY);

        String text = "RAINBOW";

        int textWidth = textManager.getStringWidth(text);

        if((boolean)values.get(tmpIndex).get(0)){
            activeColor = Color.white;
            GuiInfo.drawBaseButtonGradient(this, new Color(RainbowUtil.astolfoColors(100, 100)),new Color(RainbowUtil.astolfoColors(50, 100)), outlineColorA, false);
        }else {
            activeColor = new Color(RainbowUtil.astolfoColors(100, 100));
            GuiInfo.drawBaseButton(this, fillColorA, outlineColorA);
        }
        textManager.drawString(text, x+(width-textWidth)/2, y+1,
                activeColor.getRGB(),false);

    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state == 0){
            values.get(tmpIndex).set(0, !(boolean)values.get(tmpIndex).get(0));
            if(rainbowManager.isRegistered(setting)) {
                rainbowManager.unRegister(setting);
            }else {
                rainbowManager.register(setting);
            }
        }
    }
}
