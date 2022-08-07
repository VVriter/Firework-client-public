package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;

import java.awt.*;

import static com.firework.client.Firework.textManager;

public class SubButton extends Button{
    public SubButton(Setting setting, Frame frame) {
        super(setting, frame);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
       /* RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), Firework.colorManager.getRomeo(), Firework.colorManager.getJuliet());
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), new Color(1, 1, 1, 24)); */
        RenderRound.drawGradientRound((float) x+1, (float) y+2, (float) (width-2), (float) (height-2.5),1,Firework.colorManager.getRomeo(),Firework.colorManager.getRomeo(),Firework.colorManager.getJuliet(),Firework.colorManager.getJuliet());

        textManager.drawString(setting.name, (float) (x+3), (float) (y+1),
                Color.white.getRGB(),false);

        String icon = (boolean)setting.getValue() ? "=" : "+";

        textManager.drawString(icon, (float) (x + width - textManager.getStringWidth(icon) - 2), (float) (y+1),
                Color.white.getRGB(),false);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 1)
            setting.setValue(!(boolean) setting.getValue());
    }
}
