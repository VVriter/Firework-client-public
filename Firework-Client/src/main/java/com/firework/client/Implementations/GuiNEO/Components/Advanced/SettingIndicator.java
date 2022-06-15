package com.firework.client.Implementations.GuiNEO.Components.Advanced;

import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

public class SettingIndicator extends Button {
    public SettingIndicator(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw() {
        super.draw();
        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), new Color(ColorUtils.astolfoColors(100, 100)));
    }
}
