package com.firework.client.Implementations.UI.ConsoleGui.Components;

import com.firework.client.Features.Modules.Client.Console;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.awt.geom.Point2D;

public class DrawSquareBorderedBox {
    public static void draw() {
        //DrawGuiLeftLine
        RenderUtils2D.drawGradientRectHorizontal(new Rectangle(Console.x.getValue(),Console.y.getValue(),200,150),new Color(Console.fillStart.getValue().toRGB().getRed(),Console.fillStart.getValue().toRGB().getGreen(),Console.fillStart.getValue().toRGB().getBlue(),150),new Color(Console.fillEnd.getValue().toRGB().getRed(),Console.fillEnd.getValue().toRGB().getGreen(),Console.fillEnd.getValue().toRGB().getBlue(),150));
        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(Console.x.getValue(),Console.y.getValue(),16,150),1,Console.outlineStart.getValue().toRGB(),Console.outlineEnd.getValue().toRGB());
        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(Console.x.getValue(),Console.y.getValue(),200,10),1,Console.outlineStart.getValue().toRGB(),Console.outlineEnd.getValue().toRGB());
        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(Console.x.getValue(), Console.y.getValue(), 200, 150),1,Console.outlineStart.getValue().toRGB(),Console.outlineEnd.getValue().toRGB());
        Firework.customFontManager.drawString(" >",Console.x.getValue()+3,Console.y.getValue().floatValue()+1,Color.white.getRGB());

        //Хрестик
        Firework.customFontManager.drawString("x",Console.x.getValue()+200-6,Console.y.getValue().floatValue(),Color.white.getRGB());
    }
}
