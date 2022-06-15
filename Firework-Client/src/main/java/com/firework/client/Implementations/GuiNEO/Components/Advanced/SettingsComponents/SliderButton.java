package com.firework.client.Implementations.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.GuiNEO.GuiInfo.fillColorB;
import static com.firework.client.Implementations.GuiNEO.GuiInfo.outlineColorA;


public class SliderButton extends Button {

    public double difference;
    public float percent;
    public double renderPercent;

    public SliderButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);

        this.difference = setting.max - setting.min;
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(setting.getValue().toString());

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangle(new Rectangle(x + 2, y + height - 1, (width - 4) * ((double)setting.getValue() - setting.min) / difference, 2), new Color(ColorUtils.astolfoColors(100, 100)));

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(setting.name, x+3, y+1,
                Color.WHITE.getRGB(),false);

        textManager.drawString(setting.getValue().toString(), x+width-2-textWidth, y+1,
                Color.WHITE.getRGB(),false);
    }

    public void setSettingFromX(int mouseX) {
        percent = ((float) mouseX - this.x) / ((float) this.width);
        double result = (Double) this.setting.min + (double) ((float) this.difference * percent);
        this.setting.setValue((double) Math.round(10.0 * result) / 10.0);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        setSettingFromX(mouseX);
    }
}
