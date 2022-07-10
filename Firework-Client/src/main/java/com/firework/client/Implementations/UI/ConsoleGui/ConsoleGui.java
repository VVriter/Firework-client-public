package com.firework.client.Implementations.UI.ConsoleGui;

import com.firework.client.Features.Modules.Client.Console;
import com.firework.client.Implementations.UI.ConsoleGui.Components.DrawSquareBorderedBox;
import com.firework.client.Implementations.UI.ConsoleGui.Components.LinesNumerator;
import net.minecraft.client.gui.GuiScreen;
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
        LinesNumerator.doNumeratorStuff();



        if (Console.boolTest.getValue()) {
        /*    ConsoleMessage.log("MINECRAFT","Logger message!",Console.x.getValue()+3, Console.y.getValue().floatValue()+10);
            ConsoleMessage.error("ERROR","Error message!",Console.x.getValue()+3,Console.y.getValue().floatValue()+20);
            ConsoleMessage.warning("WARNING","Warning message",Console.x.getValue()+3,Console.y.getValue().floatValue()+30);
            ConsoleMessage.rainbow("RAINBOW","Rainbow message",Console.x.getValue()+3,Console.y.getValue().floatValue()+40); */
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
