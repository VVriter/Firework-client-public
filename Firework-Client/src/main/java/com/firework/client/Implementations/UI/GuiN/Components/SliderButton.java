package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static java.lang.Math.round;

public class SliderButton extends Button{
    public double difference;
    public SliderButton(Setting setting, Frame frame) {
        super(setting, frame);
        this.difference = setting.max - setting.min;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        double textWidth = Firework.customFontManager.getWidth(setting.getValue().toString());

        GuiInfo.drawButtonBase(this);

        RenderUtils2D.drawRectangle(new Rectangle(x + 2, y + height - 1, (width - 4) * (Double.valueOf(setting.getValue().toString()) - setting.min) / difference, 1), Firework.colorManager.getColor());

        Firework.customFontManager.drawString(setting.name, x+3, (float) (y+1),
                Color.WHITE.getRGB());

        Firework.customFontManager.drawString(setting.getValue().toString(), x+width-2-textWidth, (float) (y+1),
                Color.WHITE.getRGB());
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 0){
            setSettingFromX(mouseX);
            GuiN.isDragging = true;
        }
    }

    public void setSettingFromX(int mouseX) {
        if(setting.getValue() instanceof Double) {
            double percent = ((double) mouseX - this.x) / ((double) this.width);
            double result = this.setting.min + ((float) this.difference * percent);
            this.setting.setValue((double) round(10.0 * result) / 10.0);
        }else if(setting.getValue() instanceof Integer){
            double percent = ((double) mouseX - this.x) / ((double) this.width);
            int result = (int)this.setting.min + (int) round((float) this.difference * percent);
            this.setting.setValue(result);
        }
    }
}
