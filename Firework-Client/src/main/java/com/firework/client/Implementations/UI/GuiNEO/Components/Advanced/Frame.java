package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

public class Frame extends Button {

    public Frame(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x, y, width, height), 6, GuiInfo.outlineFrameColorA);
        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x, y, width, height), 2, GuiInfo.outlineFrameColorB);
        super.draw(mouseX, mouseY);
    }
}
