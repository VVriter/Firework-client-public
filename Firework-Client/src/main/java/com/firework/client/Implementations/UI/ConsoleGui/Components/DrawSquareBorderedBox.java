package com.firework.client.Implementations.UI.ConsoleGui.Components;

import com.firework.client.Features.Modules.Client.Console;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

public class DrawSquareBorderedBox {
    public static void draw() {
        //DrawGuiLeftLine
        RenderUtils2D.drawGradientRectHorizontal(new Rectangle(Console.x.getValue(),Console.y.getValue(),200,150), Console.fillStart.getValue().toRGB(),Console.fillEnd.getValue().toRGB());
        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(Console.x.getValue(),Console.y.getValue(),16,150),1,Console.outlineStart.getValue().toRGB(),Console.outlineEnd.getValue().toRGB());
        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(Console.x.getValue(),Console.y.getValue(),200,10),1,Console.outlineStart.getValue().toRGB(),Console.outlineEnd.getValue().toRGB());
        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(Console.x.getValue(), Console.y.getValue(), 200, 150),1,Console.outlineStart.getValue().toRGB(),Console.outlineEnd.getValue().toRGB());
        Firework.customFontManager.drawString(" >",Console.x.getValue()+3,Console.y.getValue().floatValue()+1,Color.white.getRGB());
    }
}
