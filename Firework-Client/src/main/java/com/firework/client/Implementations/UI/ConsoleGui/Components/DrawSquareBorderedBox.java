package com.firework.client.Implementations.UI.ConsoleGui.Components;

import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.awt.geom.Point2D;

import static com.firework.client.Features.Modules.Module.mc;

public class DrawSquareBorderedBox {
    public static void draw() {
        //DrawGuiLeftLine
        ScaledResolution sr = new ScaledResolution(mc);
        Point2D.Double center = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);

        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(70,100,200,10),3,new Color(0x0E0EC9),new Color(0xD213D2));
        RenderUtils2D.drawGradientRectangleOutline(new Rectangle(70, 100, 200, 150),3,new Color(0x0E0EC9),new Color(0xD213D2));
    }
}
