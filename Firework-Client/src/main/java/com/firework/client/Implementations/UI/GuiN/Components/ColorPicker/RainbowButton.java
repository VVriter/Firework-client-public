package com.firework.client.Implementations.UI.GuiN.Components.ColorPicker;

import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

public class RainbowButton extends Component {

    Setting<HSLColor> setting;
    public RainbowButton(Setting setting, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        String text = "RAINBOW";
        double textWidth = Firework.customFontManager.getWidth(text);

        if(registered())
            RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), Firework.colorManager.getRomeo(), Firework.colorManager.getJuliet());
        Firework.customFontManager.drawString(text, x + (width - textWidth)/2, (float) y + 1, registered() ? Color.white.getRGB() : Firework.colorManager.getJuliet().getRGB());
    }

    public boolean registered(){
        return Firework.rainbowManager.isRegistered(setting);
    }

    @Override
    public void init(int mouseX, int mouseY, int state) {
        super.init(mouseX, mouseY, state);
        if(state == 0) {
            if (registered())
                Firework.rainbowManager.unRegister(setting);
            else
                Firework.rainbowManager.register(setting);
        }
    }
}
