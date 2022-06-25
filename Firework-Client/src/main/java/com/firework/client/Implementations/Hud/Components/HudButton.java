package com.firework.client.Implementations.Hud.Components;

import com.firework.client.Implementations.Hud.HudGui;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Utill.Client.Pair;
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
        super(x, y, width, height);
        this.hudComponent = hudComponent;
    }

    @Override
    public void draw() {
        super.draw();
        if(this.width < 65) this.width = 65;
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), fillColorA);
        customFontManager.drawString(hudComponent.name, x  + 2, y + 2 + (8 - customFontManager.getHeight())/2 + 1, Color.white.getRGB());
        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y + HudGui.buttonHeight, width, hudComponent.height), 1, new Color(ColorUtils.astolfoColors(100, 100)));
        RenderUtils2D.drawCheckBoxV1(new Rectangle(x + width - 30 - 2, y + 2, 30, 8), hudComponent.enabled);
        hudComponent.x = x;
        hudComponent.y = y + HudGui.buttonHeight;
    }

    @Override
    public boolean initialize(Vec2f point, int state) {
        if(state == 0) {
            hudComponent.setEnabled(!hudComponent.enabled);
            return false;
        }else if(state == 1){
            if(!HudGui.isDragging.one) {
                HudGui.isDragging = new Pair<>(true, point);
            }else {
                float newX = x + point.x - HudGui.isDragging.two.x;
                float newY = y + point.y - HudGui.isDragging.two.y;
                hudComponent.x = (int) newX;
                hudComponent.y = (int) newY + HudGui.buttonHeight;
                HudGui.isDragging.two = point;
            }
            return true;
        }
        return false;
    }
}
