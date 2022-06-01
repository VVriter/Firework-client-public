package com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.Gui.GuiInfo.*;

public class ColorRainbowButton extends Button {

    public Setting setting;

    public ColorRainbowButton(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);

        this.setting = setting;
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;

        String text = "RAINBOW";

        int textWidth = textManager.getStringWidth(text);

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorA);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(text, x+3, y+1,
                new Color(ColorUtils.astolfoColors(100, 100)).getRGB(),false);

    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state == 0){

        }
    }
}
