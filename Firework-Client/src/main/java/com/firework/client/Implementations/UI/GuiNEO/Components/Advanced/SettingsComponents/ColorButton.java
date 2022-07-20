package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.math.Vec2f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static com.firework.client.Firework.settingManager;
import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.*;
import static com.firework.client.Implementations.UI.GuiNEO.GuiValueStorage.values;

public class ColorButton extends Button {
    public ColorButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.offset = setting.opened ? 11 + 40 : 11; this.originOffset = this.offset;
    }

    public void drawBase(){
        int outlineWidth = 3;

        GuiInfo.drawBaseButton(this, fillColorB, outlineColorA);

        RenderUtils2D.drawRectangle(new Rectangle(x + width-11, y+2, 7,
                7), ((HSLColor) setting.getValue()).toRGB());
        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x + width-11, y+2, 7,
                7), outlineWidth, outlineColorC);

        textManager.drawString(setting.name, x+3, y+1,
                Color.WHITE.getRGB(),false);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        drawBase();
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state==1){
            setting.opened = !setting.opened;
            settingManager.updateSettingsByName(setting);
        }
    }
}
