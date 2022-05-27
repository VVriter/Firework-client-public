package com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents;

import com.firework.client.Firework;
import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;
import java.awt.geom.Point2D;

import static com.firework.client.Firework.settingManager;
import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.Gui.GuiInfo.fillColorB;
import static com.firework.client.Implementations.Gui.GuiInfo.outlineColorA;

public class ColorButton extends Button {

    public Setting setting;
    public ColorButton(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;

        this.offset = setting.opened ? 66 : 11;
        this.height = setting.opened ? 55 : 11;
    }

    public void drawBase(){
        int outlineWidth = 3;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        RenderUtils2D.drawRectangle(new Rectangle(x+3*width/4, y+2, 7, 7), (Color) setting.getValue());
        RenderUtils2D.drawRectangleOutline(new Rectangle(x+3*width/4, y+2, 7,
                7), outlineWidth, outlineColorA);

        textManager.drawString(setting.name, x+3, y+1,
                Color.WHITE.getRGB(),false);
    }

    @Override
    public void draw() {
        super.draw();

        drawBase();
        if(setting.opened) {
            RenderUtils2D.drawRectangle(new Rectangle(x, y+11, width, height), fillColorB);
            RenderUtils2D.drawColorPickerBase(new Point(x + width/2, y + height/2), (HSLColor) setting.getValue(), 20);
        }
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state==1){
            setting.opened = !setting.opened;
            settingManager.updateSettingsByName(setting);
            offset = setting.opened ? 66 : 11;
            height = setting.opened ? 55 : 11;
        }
    }
}
