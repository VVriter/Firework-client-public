package com.firework.client.Features.IngameGuis.GuiDisconnected;

import com.firework.client.Features.Modules.Client.Client;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
public class DisconnectedGui extends GuiScreen {
    public DisconnectedGui(){
        super();
    }

    @Override
    public void initGui() {
        int i = this.height / 4 + 48;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, i + 72 - 34, 200,
                20, "MainMenu"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, i + 72 - 12, 200,
                20, "Reconnect"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        } else if (button.id == 2) {
            mc.displayGuiScreen(new GuiConnecting(this, this.mc, Client.lastData == null ? this.mc.getCurrentServerData() : Client.lastData));
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        drawDefaultBackground();
        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
