package com.firework.client.Implementations.Gui.Components.Advanced;

import com.firework.client.Features.Modules.Client.Gui;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Gui.GuiInfo;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Gui.GuiInfo.*;
import static com.firework.client.Implementations.Utill.Client.MathUtil.*;
import static java.awt.Color.*;

public class StartBlock extends Button {

    public String name;

    public StartBlock(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
        this.offset = 15;
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(name);

        //RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x-2, y, width+4
        //        , height), new HSLColor(165, 50, 50).toRGB(), new HSLColor(333, 50, 50).toRGB());

        RenderUtils2D.drawGradientRectVertical(new Rectangle(x-2, y, width+4,
                height), Gui.downStartBlockColor.getValue().toRGB(), Gui.upStartBlockColor.getValue().toRGB());

        textManager.drawString(name, x+3, y+3,
                white.getRGB(),true);

        /*if(GuiInfo.hasCategoryIcon(name)){
            mc.getTextureManager().bindTexture(GuiInfo.resourceLocationByCategory(name));
            RenderUtils2D.drawCompleteImage(x + width-15, y, 15, 15);
        }*/
    }
}
