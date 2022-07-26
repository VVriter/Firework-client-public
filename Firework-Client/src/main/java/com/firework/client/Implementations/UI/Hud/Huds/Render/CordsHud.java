package com.firework.client.Implementations.UI.Hud.Huds.Render;

import com.firework.client.Implementations.Managers.CoordsManager;
import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.UI.Hud.HudInfo;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Render.*;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;

@HudManifest(name = "CordsHud")
public class CordsHud extends HudComponent {
    @Override
    public void initialize() {
        super.initialize();
        this.height = 12;
        this.y = 100;
        this.x = 200;
        initialized = true;
    }

    @Override
    public void draw() {
        super.draw();
        EntityPlayer me = mc.player;
        //Overworld only
        String cordsInline = "X: " + Math.round(me.posX) + " Y: " + Math.round(me.posY) + " Z: " + Math.round(me.posZ);
        String overWorldCordsinNether = CoordsManager.getCordsForDrawString();

        if (!enabled && !(mc.currentScreen instanceof HudGui)) return;

        if (mc.player == null && mc.world == null) return;

        //Draws hud background
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), HudInfo.fillColorA);

        if(!mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell")){
            double textWidth = customFontManager.getWidth(cordsInline);
            this.width = (int) textWidth+5;
            customFontManager.drawString(cordsInline, x + 3, y + 2, Color.LIGHT_GRAY.getRGB());
        } else if (mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell")){
            double textWidth2 = customFontManager.getWidth(overWorldCordsinNether);
            this.width = (int) textWidth2+5;
            customFontManager.drawString(overWorldCordsinNether,x+3,y+2,Color.LIGHT_GRAY.getRGB());
        }
    }
}