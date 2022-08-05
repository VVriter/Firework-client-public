package com.firework.client.Implementations.UI.Hud.Huds.Pvp;

import com.firework.client.Firework;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
    float health;
    @Override
    public void onRender() {
        super.onRender();
        target = PlayerUtil.getClosestTarget();
        if (target == null) return;
        health = target.getHealth();
        RenderRound.drawRound(x,y,width,height,10,true, new Color(227, 217, 217, 139));
        //Draw Target heads
        GlStateManager.pushMatrix();
        ((AbstractClientPlayer)target).getLocationSkin();
        mc.getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x + 5, y + 5, 8.0F, 8, 8, 8, height - 10, height - 10, 64.0F, 64.0F);
        GlStateManager.popMatrix();
        RenderRound.drawRound(x+50,y+31,90,10,5,true,new Color(1,1,1, 200));
        RenderRound.drawRound((float) (x+51), y+32, health*4,8,4, true, Firework.colorManager.getColor());
        try {
            //Draws Target name
            String playerInfo = target.getDisplayNameString() + "|" + mc.getConnection().getPlayerInfo(target.getUniqueID()).getResponseTime() + "ms";
            customFontManager.drawString(playerInfo, 10+x + width - 120 - 2 + (120 - customFontManager.getWidth(playerInfo)) / 2, y + 2, Color.white.getRGB());
            int maxHealh = (int) target.getMaxHealth();
        } catch (Exception e){

        }


        //Draws Target armor
        int newX = x + width - 120;
    /*    for (ItemStack itemStack : target.inventory.armorInventory) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, newX+35, y + height - 2 - 10 - 7 - 12 - 2 -2);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer,itemStack, newX+35, y + height - 2 - 10 - 7 - 12 -2);
            newX += 18;
        } */


    }
}
