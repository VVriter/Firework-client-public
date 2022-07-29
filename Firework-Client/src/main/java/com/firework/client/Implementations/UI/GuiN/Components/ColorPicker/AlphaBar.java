package com.firework.client.Implementations.UI.GuiN.Components.ColorPicker;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static java.lang.Math.round;

public class AlphaBar extends Component {

    public Setting<HSLColor> setting;
    public int difference = 1;
    public AlphaBar(Setting setting, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        RenderUtils2D.drawAlphaBarBase(new Rectangle(x, y+2, width, height-4));

        float hue = this.setting.getValue().hue;
        RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y+2, width, height - 4), new HSLColor(hue, 50, 50).toRGB(), new Color(1, 1, 1, 0));
        RenderUtils2D.drawRectangle(new Rectangle((x + round(width * setting.getValue().alpha) / difference) - 0.5, y, 1, height), Color.white);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 0){
            double percent = ((float) mouseX - (this.x + 3)) / ((float) this.width - 6);
            float result = (float) (0 + this.difference * percent);
            if(result > 1)
                result = 1;
            if(result < 0)
                result = 0;

            float saturation = this.setting.getValue().saturation;
            float light = this.setting.getValue().light;
            float hue = this.setting.getValue().hue;

            this.setting.setValue(new HSLColor(hue, saturation, light, result));
            GuiN.isDragging = true;
        }
    }
}
