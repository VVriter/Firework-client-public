package com.firework.client.Implementations.UI.GuiN.Components.ColorPicker;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

public class ColorField extends Component {

    Setting<HSLColor> setting;
    public ColorField(Setting setting, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw() {
        super.draw();
        float hue = this.setting.getValue().hue;
        RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), new HSLColor(hue, 50, 50).toRGB(), Color.white);
        RenderUtils2D.drawGradientRectVertical(new Rectangle(x, y, width, height), Color.black, new Color(1, 1, 1, 0));
    }
}
