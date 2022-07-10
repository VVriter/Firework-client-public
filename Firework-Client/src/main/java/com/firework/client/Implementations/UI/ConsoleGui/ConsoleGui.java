package com.firework.client.Implementations.UI.ConsoleGui;

import com.firework.client.Features.Modules.Client.Console;
import com.firework.client.Firework;
import com.firework.client.Implementations.UI.ConsoleGui.Components.DrawSquareBorderedBox;
import com.firework.client.Implementations.UI.ConsoleGui.Messages.ConsoleMessage;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

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
        Firework.customFontManager.drawString(">",73,101,new Color(RainbowUtil.generateRainbowFadingColor(1,true)).getRGB());
        if (Console.boolTest.getValue()) {
            ConsoleMessage.log("MINECRAFT","Logger message!",73, 110);
            ConsoleMessage.error("ERROR","Error message!",73,120);
            ConsoleMessage.warning("WARNING","Warning message",73,130);
            ConsoleMessage.rainbow("RAINBOW","Rainbow message",73,140);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
