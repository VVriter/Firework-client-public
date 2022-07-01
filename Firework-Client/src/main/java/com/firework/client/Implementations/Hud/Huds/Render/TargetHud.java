package com.firework.client.Implementations.Hud.Huds.Render;

import com.firework.client.Implementations.Hud.HudGui;
import com.firework.client.Implementations.Hud.HudInfo;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Hud.Huds.HudManifest;
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

@HudManifest(name = "TargetHud")
public class TargetHud extends HudComponent {

    private AnimationUtil animationUtil;
    private boolean autoWidth = true;
    public TargetHud(){
        animationUtil = new AnimationUtil();
    }

    @Override
    public void initialize() {
        super.initialize();
        this.height = 55;
        this.width = 200;
        this.y = 100;
        this.x = 20;
        initialized = true;
    }

    @Override
    public void draw() {
        super.draw();

        if(!enabled && !(mc.currentScreen instanceof HudGui)) return;

        if(mc.player == null && mc.world == null) return;


        EntityPlayer target = mc.player;
        if(target == null) return;
        if(autoWidth)
            animationUtil.width = 120 * target.getHealth() / target.getMaxHealth();
        autoWidth = false;

        //Draws hud background
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), HudInfo.fillColorA);
        if(target.isDead){
            mc.getTextureManager().bindTexture(resourceLocation("firework/textures/skull.png"));
            RenderUtils2D.drawCompleteImage(x + 5, y + 5, height - 10, height - 10);
            autoWidth = true;
        }else {
            //Draw Target heads
            ((AbstractClientPlayer)target).getLocationSkin();
            mc.getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
            Gui.drawScaledCustomSizeModalRect(x + 5, y + 5, 8.0F, 8, 8, 8, height - 10, height - 10, 64.0F, 64.0F);

            //Draws Target name
            String playerInfo = target.getDisplayNameString() + "|" + mc.getConnection().getPlayerInfo(target.getUniqueID()).getResponseTime() + "ms";
            customFontManager.drawString(playerInfo, x + width - 120 - 2 + (120 - customFontManager.getWidth(playerInfo))/2, y + 2, Color.white.getRGB());

            //Draws Target healthbar
            animationUtil.setValues(120 * target.getHealth() / target.getMaxHealth(), 0.3f);
            animationUtil.update();
            RenderUtils2D.drawRectangle(new Rectangle(x + width - 120 - 2, y + height - 12 - 2, animationUtil.width, 10), healthColor(target));
            RenderUtils2D.drawRectangleOutline(new Rectangle(x + width - 120 - 2, y + height - 12 - 2, 120, 10), 1, Color.white);

            //Draws health info
            float health = round(10 * target.getHealth() / (target.getMaxHealth() + target.getAbsorptionAmount()) / 10 * 100);
            customFontManager.drawString("Health", x + width - 120, y + height - 12 - 2, Color.white.getRGB());
            customFontManager.drawString(String.valueOf(health), x + width - customFontManager.getWidth(String.valueOf(health)) - 2 - 2, y + height - 12 - 2, Color.white.getRGB());

            //Draws Target armor
            int newX = x + width - 120;
            for (ItemStack itemStack : mc.player.inventory.armorInventory) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, newX, y + height - 2 - 10 - 7 - 12 - 2);
                mc.getRenderItem().renderItemOverlays(mc.fontRenderer,itemStack, newX, y + height - 2 - 10 - 7 - 12);
                newX += 18;
            }

            //Draws MainHand item
            newX = x + width - 18 - 3;
            ItemStack heldItemMain = target.getHeldItemMainhand();
            mc.getRenderItem().renderItemAndEffectIntoGUI(heldItemMain, newX, y + height - 2 - 10 - 7 - 12 - 2);

            //Draws OffHand item
            newX = x + width - 36 - 3;
            ItemStack heldItemOffHand = target.getHeldItemOffhand();
            mc.getRenderItem().renderItemAndEffectIntoGUI(heldItemOffHand, newX, y + height - 2 - 10 - 7 - 12 - 2);
            //Outlines armor block
            RenderUtils2D.drawRectangleOutline(new Rectangle(x + width - 120 - 2,y + height - 2 - 10 - 7 - 12 - 3, 72 + 3, 17), 1, new Color(RainbowUtil.astolfoColors(100, 100)));

            //Outlines held items block
            RenderUtils2D.drawRectangleOutline(new Rectangle(x + width - 36 - 3 - 2,y + height - 2 - 10 - 7 - 12 - 3, 36 + 3, 17), 1, new Color(RainbowUtil.astolfoColors(100, 100)));
        }
    }

    public Color healthColor(EntityLivingBase entity){
        return new HSLColor(90 * entity.getHealth()/entity.getMaxHealth(), 50, 50).toRGB();
    }
}
