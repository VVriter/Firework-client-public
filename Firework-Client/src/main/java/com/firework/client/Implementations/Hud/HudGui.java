package com.firework.client.Implementations.Hud;

import com.firework.client.Implementations.Hud.Components.Button;
import com.firework.client.Implementations.Hud.Components.HudButton;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.Vec2f;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static com.firework.client.Firework.*;
import static java.lang.Math.*;

public class HudGui extends GuiScreen {

    public ArrayList<Button> initializedButtons = new ArrayList<>();

    public int y;
    public int x;

    public final int buttonHeight = 10;

    public void init(){
        initializedButtons.clear();

        x = 20;
        y = 20;

        int maxHeight = 0;
        for(HudComponent hudComponent : hudManager.hudComponents){
            if(x + hudComponent.width >= mc.displayWidth)
                y += maxHeight + 20;

            HudButton hudButton = new HudButton(hudComponent, x, y, hudComponent.width, buttonHeight);
            x += hudButton.width + round(hudButton.width/7);
            initializedButtons.add(hudButton);
            if(maxHeight < hudComponent.height + hudButton.height)
                maxHeight = hudComponent.height + hudButton.height;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for(Button button : initializedButtons){
            button.draw();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) throws IOException {
        super.mouseClicked(mouseX, mouseY, state);
        for(Button button : initializedButtons){
            Vec2f mouse = new Vec2f(mouseX, mouseY);
            if(isHoveringOnTheButton(button, mouse)){
                if (button instanceof HudButton) {
                    button.initialize(mouse, state);
                }
            }
        }
    }

    public boolean isHoveringOnTheButton(Button button, Vec2f mousePoint) {
        return mousePoint.x > button.x && mousePoint.x < button.x + button.width && mousePoint.y > button.y && mousePoint.y < button.y + button.height;
    }
}
