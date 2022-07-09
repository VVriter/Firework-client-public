package com.firework.client.Implementations.UI.ConsoleGui;

import com.firework.client.Implementations.UI.ConsoleGui.Components.DrawSquareBorderedBox;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public class ConsoleGui extends GuiScreen {
    public ConsoleGui(){
        super();
    }

    @Override
    public void initGui() {

    }

    public static String text;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DrawSquareBorderedBox.draw();
    }
}
