package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.util.math.Vec2f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static com.firework.client.Firework.settingManager;
import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.*;
import static com.firework.client.Implementations.UI.GuiNEO.GuiValueStorage.values;

public class ColorPickerBase extends Button {
    private int tmpIndex;
    public ColorPickerBase(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);

        this.offset = 0; this.originOffset = this.offset;
        this.height = setting.opened ? 40 : 0; this.originHeight = this.height;

        tmpIndex = localIndex - 1;
        if(values.get(tmpIndex) == null){
            values.set(tmpIndex, new ArrayList());
            values.get(tmpIndex).add(0, false);
            values.get(tmpIndex).add(1, pointFromColor());
        }else {
            if(values.get(tmpIndex).size() > 1)
                values.get(tmpIndex).set(1, pointFromColor());
            else
                values.get(tmpIndex).add(pointFromColor());
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        if(setting.opened) {
            RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);
            RenderUtils2D.drawColorPickerBase(new Rectangle(x, y+2, width, height-2), new HSLColor(((HSLColor) setting.getValue()).hue, 50, 50).toRGB());
            RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                    height), 1, outlineColorA);

            if((boolean)values.get(tmpIndex).get(0) && Gui.isHoveringOnTheButton(new Button(x, y, width, height), new Vec2f(mouseX, mouseY))) {
                values.get(tmpIndex).set(1, new Point2D.Double(mouseX, mouseY));
            }else if((boolean)values.get(tmpIndex).get(0)){
                values.get(tmpIndex).set(0, false);
            }
            Point2D.Double p = (Point2D.Double) values.get(tmpIndex).get(1);
            setting.setValue(colorFromPoint(p));
            RenderUtils2D.drawFilledCircle(p, ((HSLColor) setting.getValue()).toRGB(), 3);
            RenderUtils2D.drawCircleOutline(p, 3, 2, Color.white);
        }
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state==1){
            setting.opened = !setting.opened;
            settingManager.updateSettingsByName(setting);
            this.originHeight = setting.opened ? 40 : 0;
        }
        if(state==0){
            if(setting.opened){
                values.get(tmpIndex).set(0, !(boolean)values.get(tmpIndex).get(0));
            }
        }
    }

    private HSLColor colorFromPoint(Point2D.Double point){
        return new HSLColor(((HSLColor) setting.getValue()).hue, (float) Math.abs(100*(point.x-x)/width), (float) Math.abs(100*(height-(point.y-y))/height), ((HSLColor) setting.getValue()).alpha);
    }

    private Point2D.Double pointFromColor(){
        float saturation = ((HSLColor) setting.getValue()).saturation;
        float light = ((HSLColor) setting.getValue()).light;
        return new Point2D.Double(x + width*saturation/100, y + height -(height)*light/100);
    }
}
