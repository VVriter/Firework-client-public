package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;

import java.awt.*;

import static com.firework.client.Firework.textManager;

public class ModeButton extends Button{
    public ModeButton(Setting setting, Frame frame) {
        super(setting, frame);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        int textWidth = textManager.getStringWidth(setting.getValue().toString());

        GuiInfo.drawButtonBase(this);

        textManager.drawString(setting.name, (float) (x+3), (float) (y+1),
                Color.WHITE.getRGB(),false);

        textManager.drawString(setting.getValue().toString(), (float) (x+width-2-textWidth), (float) (y+1),
                Firework.colorManager.getColor().getRGB(),false);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 0) {
            --setting.index;
            if (setting.index < 0) {
                setting.index = setting.list.size() - 1;
            }
            setting.setValue(setting.list.get(setting.index));
        }else if(state == 1){
            ++setting.index;
            if (setting.index > setting.list.size()-1) {
                setting.index = 0;
            }
            setting.setValue(setting.list.get(setting.index));
        }
    }
}
