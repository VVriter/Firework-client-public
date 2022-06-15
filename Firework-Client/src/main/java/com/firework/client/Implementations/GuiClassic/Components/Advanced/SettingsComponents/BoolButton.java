package com.firework.client.Implementations.GuiClassic.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.GuiClassic.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;

import java.awt.*;
import java.awt.geom.Point2D;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.fillColorB;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.outlineColorA;

public class BoolButton extends Button {

    public Color activeColor;

    public BoolButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorB);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x + width-11, y+2, 7,
                7), 1, Color.WHITE);

        if((boolean)setting.getValue()){
            activeColor = new Color(ColorUtils.astolfoColors(100, 100));
            RenderUtils2D.drawCheckMarkV2(new Point2D.Double(x + width - 11 + 3.5, y + 8), Color.WHITE);
        }else{
            activeColor = Color.WHITE;
        }

        textManager.drawString(setting.name, x+3, y+1,
                activeColor.getRGB(),false);
    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        setting.setValue(!(boolean)setting.getValue());
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }
}
