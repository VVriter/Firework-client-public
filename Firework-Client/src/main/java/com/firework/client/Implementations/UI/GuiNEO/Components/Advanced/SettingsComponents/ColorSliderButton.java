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
    public Vector vector;

    public float difference;
    public float percent;

    public ColorSliderButton(Setting setting, int x, int y, int width, int height, CSliderMode mode, Vector vector) {
        super(setting, x, y, width, height);
        this.mode = mode;
        this.vector = vector;

        if(mode == CSliderMode.HUE) {
            this.difference = 360;
        }else if (mode == CSliderMode.ALPHA){
            this.difference = 1;
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
        }else if(mode == CSliderMode.ALPHA){
            value = ((HSLColor) setting.getValue()).alpha;
            RenderUtils2D.drawHueBar(new Rectangle(x, y, width, height));
        }
        if(vector == Vector.Horizontal)
            RenderUtils2D.drawRectangle(new Rectangle((int) (x + round(width * value) / difference) - 0.5, y+1, 1, height-2), Color.white);
        else if(vector == Vector.Vertical)
            RenderUtils2D.drawRectangle(new Rectangle(x - 1, (int) (y + round(height * value) / difference), width+2, 1), Color.white);
    }

    public void setSettingFromX(int mouseX, int mouseY) {
        if(vector == Vector.Horizontal) {
            percent = ((float) mouseX - this.x) / ((float) this.width);
        }else if(vector == Vector.Vertical){
            percent = ((float) mouseY - this.y) / ((float) this.height);
        }
        float result = 0 + this.difference * percent;

        float saturation = ((HSLColor) this.setting.getValue()).saturation;
        float light = ((HSLColor) this.setting.getValue()).light;
        float hue = ((HSLColor) this.setting.getValue()).hue;

        if(mode == CSliderMode.HUE) {
            this.setting.setValue(new HSLColor(result, saturation, light));
        }else if(mode == CSliderMode.ALPHA){
            this.setting.setValue(new HSLColor(hue, saturation, light, result));
        }
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state == 0) {
            if (setting.opened != true) return;

            setSettingFromX(mouseX, mouseY);
            Gui.isDragging = true;
        }
    }

    public enum CSliderMode{
        HUE, ALPHA
    }

    public enum Vector{
        Horizontal, Vertical
    }
}
