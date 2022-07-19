package com.firework.client.Implementations.UI.Hud.Huds.Render;

import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.UI.Hud.HudInfo;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.*;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;
import static java.lang.Math.round;

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
        String cordsInline = "X: " + Math.round(me.posX) + " Y: " + Math.round(me.posY) + " Z: " + Math.round(me.posZ);
        double textWidth = customFontManager.getWidth(cordsInline);
        this.width = (int) textWidth+5;
        if (!enabled && !(mc.currentScreen instanceof HudGui)) return;

        if (mc.player == null && mc.world == null) return;

        //Draws hud background
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), HudInfo.fillColorA);
        customFontManager.drawString(cordsInline, x + 3, y + 2, Color.LIGHT_GRAY.getRGB());
    }
}