package com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Gui.Gui;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import scala.xml.Null;

import java.awt.*;

import static com.firework.client.Firework.settingManager;
import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.Gui.GuiInfo.fillColorB;
import static com.firework.client.Implementations.Gui.GuiInfo.outlineColorA;
import static java.lang.Math.*;

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

        RenderUtils2D.drawRectangle(new Rectangle(x+3*width/4, y+2, 7, 7), ((HSLColor) setting.getValue()).toRGB());
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
            RenderUtils2D.drawColorPickerBase(new Point(x + width/2, y + 11 + height/2), (HSLColor) setting.getValue(), 28);

            int height = 4;
            int width = 4;
            Point center = new Point(x + width/2, y + 11 + height/2);
            Point p = hueToPosition(center, 28, (int) ((HSLColor) setting.getValue()).hue);

            //    RenderUtils2D.drawRectangle(new Rectangle(mouse.x - height / 2, p.y - width / 2, width, height), ((HSLColor) setting.getValue()).toRGB());
            //    RenderUtils2D.drawRectangleOutline(new Rectangle(mouse.x - height / 2, p.y - width / 2, width, height), 1, fillColorB);

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
        if(state==0){
            if(setting.opened){
                double radius = 28;
                ;
            }
        }
    }

    public double angle(Point one, Point two)
    {
        return  0;
    }

    public Point hueToPosition(Point center, int r, int hue){
        double x = Math.sin(((hue * Math.PI) / 180)) * r;
        double y = Math.cos(((hue * Math.PI) / 180)) * r;

        return new Point((int) (center.getX() + x), (int) (center.getY() + y));
    }
}
