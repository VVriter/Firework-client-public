package com.firework.client.Implementations.UI.Hud.Huds;

import com.firework.client.Firework;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class HudGui extends GuiScreen {

    boolean isDragging = false;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(!isDragging) return;
        Firework.hudManager.hudComponents
                .forEach(hudComponent -> {
                    if(hudComponent.isPicked){
                        hudComponent.x = mouseX - hudComponent.xOffset;
                        hudComponent.y = mouseY - hudComponent.yOffset;
                    }
                });
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isDragging = false;
        Firework.hudManager.hudComponents
                .forEach(hudComponent -> hudComponent.isPicked = false);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) throws IOException {
        super.mouseClicked(mouseX, mouseY, state);
        Firework.hudManager.hudComponents
                .stream().filter(hudComponent -> isHoveringOnTheButton(hudComponent, mouseX, mouseY))
                .forEach(hudComponent -> {
                    if(state == 0) {
                        isDragging = true;
                        hudComponent.isPicked = true;
                        hudComponent.xOffset = mouseX - hudComponent.x;
                        hudComponent.yOffset = mouseY - hudComponent.y;
                    }
                });
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public boolean isHoveringOnTheButton(HudComponent button, int mouseX, int mouseY) {
        return mouseX > button.x && mouseX < button.x + button.width && mouseY > button.y && mouseY < button.y + button.height;
    }
}
