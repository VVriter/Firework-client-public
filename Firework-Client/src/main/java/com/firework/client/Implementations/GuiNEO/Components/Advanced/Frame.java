package com.firework.client.Implementations.GuiNEO.Components.Advanced;

import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import static com.firework.client.Implementations.GuiNEO.GuiInfo.outlineFrameColorA;
import static com.firework.client.Implementations.GuiNEO.GuiInfo.outlineFrameColorB;

public class Frame extends Button {

    public Frame(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw() {

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width, height), 6, outlineFrameColorA);
        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width, height), 2, outlineFrameColorB);
        super.draw();
    }
}
