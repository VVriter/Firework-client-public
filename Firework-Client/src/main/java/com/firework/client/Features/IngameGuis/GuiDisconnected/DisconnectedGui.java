package com.firework.client.Features.IngameGuis.GuiDisconnected;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class DisconnectedGui extends GuiScreen {

    public DisconnectedGui(){
        super();
    }


    @Override
    public void initGui() {
        int i = this.height / 4 + 48;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, i + 72 - 34, 200,
                20, "MainMenu"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 4) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
