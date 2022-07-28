package com.firework.client.Implementations.UI.GuiN;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;
import java.util.ArrayList;

public class GuiInfo {
    public static ArrayList<Frame> frames = new ArrayList<>();

    public static int barWidth = 70;
    public static int barHeight = 10;

    public static void setupFrames(){
        int x = 10;
        for(Module.Category category : Module.Category.values()){
            Frame frame = new Frame(category);
            frame.setX(x);
            x+= frame.barWidth + 10;
            frames.add(frame);
        }
    }

    public static Frame getFrame(Module.Category category){
        for(Frame frame : frames)
            if(frame.category == category)
                return frame;

        return null;
    }

    public static void drawButtonBase(Component component){
        RenderUtils2D.drawRectAlpha(new Rectangle(component.x, component.y, component.width, component.getHeight()), new Color(1,1, 1, 169));
    }

    public static boolean isHoveringOnTheComponent(Component component, int mouseX, int mouseY) {
        return mouseX > component.x && mouseX < component.x + component.width && mouseY > component.y && mouseY < component.y + component.height;
    }
}
