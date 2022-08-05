package com.firework.client.Implementations.UI.Hud.Huds.Pvp;

import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.Rounded.RoundRenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

import static com.firework.client.Firework.customFontManager;

@HudManifest(
        name = "TargetHud"
)
public class TargetHud extends HudComponent {
    @Override
    public void load() {
        super.load();
        this.x = 100;
        this.y = 20;
        this.height = 3 * 16;
        this.width = 9 * 16;
    }

    EntityPlayer target;

    @Override
    public void onRender() {
        super.onRender();
        target = mc.player; //PlayerUtil.getClosestTarget();
        if (target == null) return;

        RoundRenderUtils.drawRound(x,y,width,height,10,true, new Color(227, 217, 217, 139));
        //Draw Target heads
        ((AbstractClientPlayer)target).getLocationSkin();
        mc.getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x + 5, y + 5, 8.0F, 8, 8, 8, height - 10, height - 10, 64.0F, 64.0F);

        try {
            //Draws Target name
            String playerInfo = target.getDisplayNameString() + "|" + mc.getConnection().getPlayerInfo(target.getUniqueID()).getResponseTime() + "ms";
            customFontManager.drawString(playerInfo, x + width - 120 - 2 + (120 - customFontManager.getWidth(playerInfo)) / 2, y + 2, Color.white.getRGB());

            int health = (int) target.getHealth();
            int maxHealh = (int) target.getMaxHealth();

            RoundRenderUtils.drawRound(x+35,y+38,100,5,20,new Color(1,1,1, 200));

        } catch (Exception e){

        }
    }
}
