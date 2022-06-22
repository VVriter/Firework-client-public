package com.firework.client.Implementations.Hud.Components;

import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Hud.HudInfo.*;

public class HudButton extends Button{

    public HudComponent hudComponent;

    public HudButton(HudComponent hudComponent, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hudComponent = hudComponent;
    }

    @Override
    public void draw() {
        super.draw();
        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorA);
        customFontManager.drawString(hudComponent.name, x  + (width - customFontManager.getWidth(hudComponent.name))/2, y, Color.white.getRGB());
        hudComponent.x = x;
        hudComponent.y = y;
    }

    @Override
    public boolean initialize(Vec2f point, int state) {
        if(state == 0) {
            hudComponent.setEnabled(!hudComponent.enabled);
            return false;
        }else if(state == 1){
            return true;
        }
        return false;
    }
}
