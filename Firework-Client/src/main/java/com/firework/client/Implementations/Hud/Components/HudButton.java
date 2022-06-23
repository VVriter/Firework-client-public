package com.firework.client.Implementations.Hud.Components;

import com.firework.client.Implementations.Hud.HudGui;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Hud.HudInfo.*;

public class HudButton extends Button{

    public HudComponent hudComponent;

    public HudButton(HudComponent hudComponent, int x, int y, int width, int height) {
        super(x-2, y, width+2, height);
        this.hudComponent = hudComponent;
    }

    @Override
    public void draw() {
        super.draw();
        if(this.width < 60) this.width = 60;
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), fillColorA);
        customFontManager.drawString(hudComponent.name, x  + (width - customFontManager.getWidth(hudComponent.name))/2, y, Color.white.getRGB());
        if(hudComponent.opened)
            RenderUtils2D.drawRectangleOutline(new Rectangle(x, y + HudGui.buttonHeight, width, hudComponent.height), 0.5f, new Color(ColorUtils.astolfoColors(100, 100)));
        hudComponent.x = x;
        hudComponent.y = y;
    }

    @Override
    public boolean initialize(Vec2f point, int state) {
        if(state == 0) {
            hudComponent.setEnabled(!hudComponent.enabled);
            return false;
        }else if(state == 1){
            hudComponent.opened = !hudComponent.opened;
            return true;
        }
        return false;
    }
}
