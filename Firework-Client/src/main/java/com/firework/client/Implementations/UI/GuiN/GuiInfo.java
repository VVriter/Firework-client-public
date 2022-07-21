package com.firework.client.Implementations.UI.GuiN;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.Frame;

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

    public static boolean isHoveringOnTheComponent(Component component, int mouseX, int mouseY) {
        return mouseX > component.x && mouseX < component.x + component.width && mouseY > component.y && mouseY < component.y + component.height;
    }
}
