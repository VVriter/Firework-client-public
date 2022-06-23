package com.firework.client.Implementations.GuiClassic.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.GuiClassic.Components.Button;
import com.firework.client.Implementations.GuiClassic.Gui;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Implementations.GuiClassic.GuiInfo.fillColorB;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.outlineColorA;
import static java.lang.Math.round;

public class ColorSliderButton extends Button {

    public CSliderMode mode;

    public float difference;
    public float percent;

    public ColorSliderButton(Setting setting, int x, int y, int width, int height, CSliderMode mode) {
        super(setting, x, y, width, height);
        this.mode = mode;

        if(mode == CSliderMode.HUE) {
            this.difference = 360;
        }else if (mode == CSliderMode.SATURATION || mode == CSliderMode.LIGHT){
            this.difference = 100;
        }

        this.originOffset = setting.opened ? 13 : 0; this.offset = setting.opened ? 13 : 0;
        this.originHeight = setting.opened ? 12 : 0; this.height = setting.opened ? 12 : 0;
    }

    @Override
    public void draw() {
        if(setting.opened != true) return;
        super.draw();

        int outlineWidth = 3;
        int renderWidth = 2;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        float value = 0;
        if(mode == CSliderMode.HUE){
            value = ((HSLColor) setting.getValue()).hue;
        }else if(mode == CSliderMode.SATURATION){
            value = ((HSLColor) setting.getValue()).saturation;
            RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), new HSLColor(((HSLColor) setting.getValue()).hue, 50, 50).toRGB(), Color.GRAY);
        }else if(mode == CSliderMode.LIGHT){
            value = ((HSLColor) setting.getValue()).light;
            RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), new HSLColor(((HSLColor) setting.getValue()).hue, 50, 50).toRGB(), Color.BLACK);
        }
        RenderUtils2D.drawMarker(new Rectangle((int) (x + round(width * value - 0) / difference), y, 6, height), new Color(ColorUtils.astolfoColors(100, 100)));
    }

    public void setSettingFromX(int mouseX) {
        percent = ((float) mouseX - this.x) / ((float) this.width);
        float result = 0 + this.difference * percent;
        if(mode == CSliderMode.HUE) {
            float saturation = ((HSLColor) this.setting.getValue()).saturation;
            float light = ((HSLColor) this.setting.getValue()).light;
            this.setting.setValue(new HSLColor(result, saturation, light));
        }else if(mode == CSliderMode.SATURATION){
            float hue = ((HSLColor) this.setting.getValue()).hue;
            float light = ((HSLColor) this.setting.getValue()).light;
            this.setting.setValue(new HSLColor(hue, result, light));
        }else if(mode == CSliderMode.LIGHT){
            float hue = ((HSLColor) this.setting.getValue()).hue;
            float saturation = ((HSLColor) this.setting.getValue()).saturation;
            this.setting.setValue(new HSLColor(hue, saturation, result));
        }
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state == 0) {
            if (setting.opened != true) return;

            setSettingFromX(mouseX);
            Gui.isDragging = true;
        }
    }

    public enum CSliderMode{
        HUE, SATURATION, LIGHT
    }
}
