package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.*;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.multiplayer.GuiConnecting;

import java.awt.*;

import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.fillColorB;
import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.outlineColorA;
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

        this.offset = setting.opened ? 11 : 0; this.originOffset = this.offset;
        this.height = setting.opened ? 10 : 0; this.originHeight = this.height;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(setting.opened != true) return;
        super.draw(mouseX, mouseY);

        GuiInfo.drawBaseButton(this, fillColorB, outlineColorA, false);

        float value = 0;
        if(mode == CSliderMode.HUE){
            value = ((HSLColor) setting.getValue()).hue;
            RenderUtils2D.drawHueBar(new Rectangle(x, y+3, width, height-6));
        }else if(mode == CSliderMode.SATURATION){
            value = ((HSLColor) setting.getValue()).saturation;
            RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), new HSLColor(((HSLColor) setting.getValue()).hue, 50, 50).toRGB(), Color.GRAY);
        }else if(mode == CSliderMode.LIGHT){
            value = ((HSLColor) setting.getValue()).light;
            RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), new HSLColor(((HSLColor) setting.getValue()).hue, 50, 50).toRGB(), Color.BLACK);
        }
        RenderUtils2D.drawRectangle(new Rectangle((int) (x + round(width * value) / difference) - 1, y+1, 2, height-2), Color.white);
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
