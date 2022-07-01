package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced;

import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

public class EndBlock extends Button {
    public EndBlock(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw() {
        super.draw();
        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), new Color(RainbowUtil.astolfoColors(100, 100)));
    }
}
