package com.firework.client.Implementations.UI.GuiN.Components.ColorPicker;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;
import java.awt.geom.Point2D;

public class ColorField extends Component {

    Setting<HSLColor> setting;
    public ColorField(Setting setting, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        float hue = this.setting.getValue().hue;
        RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), new HSLColor(hue, 50, 50).toRGB(), Color.white);
        RenderUtils2D.drawGradientRectVertical(new Rectangle(x, y, width, height), Color.black, new Color(1, 1, 1, 0));

        if(GuiN.isDragging && GuiInfo.isHoveringOnTheComponent(this, mouseX, mouseY)){
            setting.setValue(colorFromPoint(new Point2D.Double(mouseX, mouseY)));
        }
        RenderUtils2D.drawFilledCircle(pointFromColor(), new HSLColor(setting.getValue().hue, setting.getValue().saturation, setting.getValue().light).toRGB(), 3);
        RenderUtils2D.drawCircleOutline(pointFromColor(), 3, 2, Color.white);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        GuiN.isDragging = true;
    }

    private HSLColor colorFromPoint(Point2D.Double point){
        return new HSLColor(setting.getValue().hue, (float) Math.abs(100*(point.x-x)/width), (float) Math.abs(50*(height-(point.y-y))/height), setting.getValue().alpha);
    }

    private Point2D.Double pointFromColor(){
        float saturation = setting.getValue().saturation;
        float light = setting.getValue().light;
        return new Point2D.Double(x + width*saturation/100, y + height -(height)*light/50);
    }
}
