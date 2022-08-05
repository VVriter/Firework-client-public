package com.firework.client.Implementations.UI.Hud.Huds.Misc;

import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.CoordsManager;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

import static com.firework.client.Firework.customFontManager;

@HudManifest(
        name = "Cords"
)
public class Cords extends HudComponent {

    @Override
    public void load() {
        super.load();
        this.x = 200;
        this.y = 120;
        this.height = 3 * 16;
        this.width = 9 * 16;
    }

    @Override
    public void onRender() {
        super.onRender();
        EntityPlayer me = mc.player;
        String cordsInline = "X: " + Math.round(me.posX) + " Y: " + Math.round(me.posY) + " Z: " + Math.round(me.posZ);
        String overWorldCordsinNether = CoordsManager.getCordsForDrawString();
        RenderRound.drawRound(x,y+1,width,10,4,true, Firework.colorManager.getColor());
        if(!mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell")){
            double textWidth = customFontManager.getWidth(cordsInline);
            this.width = (int) textWidth+5;
            customFontManager.drawString(cordsInline, x + 3, y + 2, Color.LIGHT_GRAY.getRGB());
        } else if (mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell")){
            double textWidth2 = customFontManager.getWidth(overWorldCordsinNether);
            this.width = (int) textWidth2+5;
            customFontManager.drawString(overWorldCordsinNether,x+3,y+2,Color.WHITE.getRGB());
        }
    }
}
