package com.firework.client.Implementations.UI.GuiN.Components.ColorPicker;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents.ColorSliderButton;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static java.lang.Math.round;

public class HueBar extends Component {

    public Setting<HSLColor> setting;
    public int difference = 360;
    public HueBar(Setting setting, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw() {
        super.draw();
        RenderUtils2D.drawHueBar(new Rectangle(x, y+3, width, height-6));
        RenderUtils2D.drawRectangle(new Rectangle((x + round(width * setting.getValue().hue) / difference) - 0.5, y+1, 1, height-2), Color.white);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 0){
            double percent = ((float) mouseX - this.x) / ((float) this.width);
            float result = (float) (0 + this.difference * percent);

            float saturation = this.setting.getValue().saturation;
            float light = this.setting.getValue().light;
            float alpha = this.setting.getValue().alpha;

            this.setting.setValue(new HSLColor(result, saturation, light, alpha));
            GuiN.isDragging = true;
        }
    }
}
