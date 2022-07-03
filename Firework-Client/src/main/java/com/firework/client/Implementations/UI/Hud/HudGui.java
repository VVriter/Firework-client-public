package com.firework.client.Implementations.UI.Hud;

import com.firework.client.Implementations.UI.Hud.Components.Button;
import com.firework.client.Implementations.UI.Hud.Components.HudButton;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;

import static com.firework.client.Firework.*;
import static java.lang.Math.*;

public class HudGui extends GuiScreen {

    public static ArrayList<com.firework.client.Implementations.UI.Hud.Components.Button> initializedButtons = new ArrayList<>();

    public static int y;
    public static int x;

    public static final int buttonHeight = 12;

    public static ScaledResolution scaledResolution;
    public HudGui(){
        init();
    }

    public static void init(){
        initializedButtons.clear();

        x = 20;
        y = 20;

        int maxHeight = 0;
        for(HudComponent hudComponent : hudManager.hudComponents){
            if(!hudComponent.initialized) {
                if (hudComponent.enabled) {
                    if (x + hudComponent.width >= 300) {
                        y += maxHeight + 20;
                        x = 20;
                    }

                    HudButton hudButton = new HudButton(hudComponent, x, y, hudComponent.width, buttonHeight);
                    x += hudButton.width + round(hudButton.width / 7);
                    initializedButtons.add(hudButton);
                    if (maxHeight < hudComponent.height + hudButton.height)
                        maxHeight = hudComponent.height + hudButton.height;
                }
            }else{
                HudButton hudButton = new HudButton(hudComponent, hudComponent.x, hudComponent.y - HudGui.buttonHeight, hudComponent.width, buttonHeight);
                initializedButtons.add(hudButton);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for(HudComponent hudComponent : hudManager.hudComponents)
            hudComponent.draw();

        for(com.firework.client.Implementations.UI.Hud.Components.Button button : initializedButtons){
            button.draw();
        }

        boolean shouldInit = false;
        for(HudComponent hudComponent : hudManager.hudComponents){
            if(hudComponent.picked){
                hudComponent.x = (int) (mouseX - hudComponent.offsetX);
                hudComponent.y = (int) (mouseY - hudComponent.offsetY);
                shouldInit = true;
            }
        }

        if(shouldInit)
            init();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) {
        boolean shouldInit = false;
        for(com.firework.client.Implementations.UI.Hud.Components.Button button : initializedButtons){
            Vec2f mouse = new Vec2f(mouseX, mouseY);
            if(isHoveringOnTheButton(button, mouse)){
                scaledResolution = new ScaledResolution(mc);
                if (button instanceof HudButton) {
                    shouldInit = button.initialize(mouse, state);
                }
            }
        }
        if(shouldInit)
            init();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public boolean isHoveringOnTheButton(Button button, Vec2f mousePoint) {
        return mousePoint.x > button.x && mousePoint.x < button.x + button.width && mousePoint.y > button.y && mousePoint.y < button.y + button.height;
    }
}