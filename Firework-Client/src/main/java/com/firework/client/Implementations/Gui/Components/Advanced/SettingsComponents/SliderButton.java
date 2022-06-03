package com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;


import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.Gui.GuiInfo.fillColorB;
import static com.firework.client.Implementations.Gui.GuiInfo.outlineColorA;


public class SliderButton extends Button {

    public double difference;
    public float percent;
    public double renderPercent;

    public SliderButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);

        this.originOffset = setting.opened ? 13 : 0; this.offset = setting.opened ? 13 : 0;
        this.originHeight = setting.opened ? 12 : 0; this.height = setting.opened ? 12 : 0;

        this.difference = setting.max - setting.min;
    }

    @Override
    public void draw() {
        if(setting.opened != true) return;
        this.originOffset = setting.opened ? 13 : 0;
        this.originHeight = setting.opened ? 12 : 0;
        super.draw();

        int outlineWidth = 3;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width * ((double)setting.getValue() - setting.min) / difference, height), new Color(ColorUtils.astolfoColors(100, 100)));

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(setting.name + ":" + setting.getValue(), x+3, y+1,
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
