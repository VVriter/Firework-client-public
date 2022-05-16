package com.firework.client.Implementations.Gui.Components.Advanced;

import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Gui.GuiInfo.*;

public class StartBlock extends Button {

    public String name;

    public StartBlock(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(name);

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorA);
        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(name, x+3, y+3,
                new Color(ColorUtils.astolfoColors(100, 100)).getRGB(),true);
    }
}
