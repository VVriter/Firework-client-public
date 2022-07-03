package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.renderer.entity.Render;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static com.firework.client.Firework.settingManager;
import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.*;
import static com.firework.client.Implementations.UI.GuiNEO.GuiValueStorage.values;

public class ColorButton extends Button {
    private int tmpIndex;
    public ColorButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);

        this.offset = setting.opened ? 10 + 40 : 10; this.originOffset = this.offset;
        this.height = setting.opened ? 40 : 10; this.originHeight = this.height;

        tmpIndex = localIndex - 1;
        if(values.get(tmpIndex) == null){
            values.set(tmpIndex, new ArrayList());
            values.get(tmpIndex).add(0, false);
        }
    }

    public void drawBase(){

        int outlineWidth = 3;

        GuiInfo.drawBaseButton(this, fillColorB, outlineColorA);

        RenderUtils2D.drawRectangle(new Rectangle(x + width-11, y+2, 7,
                7), ((HSLColor) setting.getValue()).toRGB());
        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x + width-11, y+2, 7,
                7), outlineWidth, outlineColorC);

        textManager.drawString(setting.name, x+3, y+1,
                Color.WHITE.getRGB(),false);
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        int radius = 24;

        drawBase();
        if(setting.opened) {
            RenderUtils2D.drawRectangle(new Rectangle(x, y+11, width, height), fillColorB);
            RenderUtils2D.drawColorPickerBase(new Rectangle(x, y+11, width, height), new HSLColor(((HSLColor) setting.getValue()).hue, 50, 50).toRGB());
            RenderUtils2D.drawRectangleOutline(new Rectangle(x, y + 11, width,
                    height), 1, outlineColorA);

            Point2D.Double p;
            if((boolean)values.get(tmpIndex).get(0)) {
                 p = new Point2D.Double(mouseX, mouseY);
            }else{
                float saturation = ((HSLColor) setting.getValue()).saturation;
                float light = ((HSLColor) setting.getValue()).light;
                p = new Point2D.Double(x + width*saturation/100, y + 11 + height -(height)*light/100);
            }

            RenderUtils2D.drawFilledCircle(p, colorFromPoint(p).toRGB(), 3);
            RenderUtils2D.drawCircleOutline(p, 3, 2, Color.white);
        }
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state==1){
            setting.opened = !setting.opened;
            settingManager.updateSettingsByName(setting);
            this.originOffset = setting.opened ? 40 + 10 : 10;
            this.originHeight = setting.opened ? 40 : 10;
        }
        if(state==0){
            if(setting.opened){
               if(mouseY >= y + 10){
                   values.get(tmpIndex).set(0, !(boolean)values.get(tmpIndex).get(0));
               }
            }
        }
    }

    private HSLColor colorFromPoint(Point2D.Double point){
        return new HSLColor(((HSLColor) setting.getValue()).hue, (float) ((point.x-x)/width*100), (float) (point.y-y-11*100/height));
    }
}
