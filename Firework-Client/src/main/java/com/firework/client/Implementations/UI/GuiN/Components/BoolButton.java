package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.Utill.Render.AnimationUtil;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Firework.textManager;

public class BoolButton extends Button{

    AnimationUtil animation;

    public BoolButton(Setting setting, Frame frame) {
        super(setting, frame);
        this.animation = new AnimationUtil();
        if((boolean)setting.getValue())
            animation.width = 7;
        else
            animation.width = 3;
    }

    @Override
    public void draw() {
        super.draw();
        animation.update();

        GuiInfo.drawButtonBase(this);

        int activeColor = (boolean) setting.getValue() ? RainbowUtil.astolfoColors(100, 100) : Color.white.getRGB();

        RenderUtils2D.drawCheckMarkV3(new Rectangle(x + width - 10 - 2, y + 2, 10, 6), (Boolean) setting.getValue(), animation.width);
        textManager.drawString(setting.name, (float) (x+3), (float) (y+1),
                activeColor,false);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state != 0) return;
        if(GuiN.isDragging) return;
        setting.setValue(!(boolean)setting.getValue());
        if((boolean)setting.getValue())
            animation.setValues(7, 1f);
        else
            animation.setValues(3, 1f);
    }
}
