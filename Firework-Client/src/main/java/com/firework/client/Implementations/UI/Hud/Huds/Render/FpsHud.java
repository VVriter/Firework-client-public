package com.firework.client.Implementations.UI.Hud.Huds.Render;


import com.firework.client.Implementations.Managers.FpsManager;
import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.UI.Hud.HudInfo;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Render.*;
import com.firework.client.Implementations.Utill.Render.Rectangle;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;
import static java.lang.Math.round;

@HudManifest(name = "FpsHud")
public class FpsHud extends HudComponent {

    @Override
    public void initialize() {
        super.initialize();
        this.height = 12;
        this.y = 100;
        this.x = 350;
        initialized = true;
    }

    @Override
    public void draw() {
        super.draw();
        String toRender = String.valueOf("FPS: " + FpsManager.getCurrendFps());
        //Draws hud background
        if(!enabled && !(mc.currentScreen instanceof HudGui)) return;
        if(mc.player == null && mc.world == null) return;

        this.width = (int) (customFontManager.getWidth(toRender)+5);

        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), HudInfo.fillColorA);
        customFontManager.drawString(toRender,x+3,y+2, Color.LIGHT_GRAY.getRGB());
    }
}