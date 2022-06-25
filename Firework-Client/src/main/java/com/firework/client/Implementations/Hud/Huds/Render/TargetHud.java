package com.firework.client.Implementations.Hud.Huds.Render;

import com.firework.client.Implementations.Hud.HudGui;
import com.firework.client.Implementations.Hud.HudInfo;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.EntityRenderBuilder2D;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;
import static java.lang.Math.round;

@HudManifest(name = "TargetHud")
public class TargetHud extends HudComponent {

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


        EntityPlayer target = PlayerUtil.getClosestTarget();
        if(target == null) return;

        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), HudInfo.fillColorA);
        if(target.isDead){
            mc.getTextureManager().bindTexture(resourceLocation("firework/textures/skull.png"));
            RenderUtils2D.drawCompleteImage(x + 15, y + 15, height-20, height-20);
            //customFontManager.drawString("PLAYER LOST");
        }else {
            EntityRenderBuilder2D entityRenderBuilder2D = new EntityRenderBuilder2D(target)
                    .setPosition(x + 30, y + 50)
                    .setRotationToCursor(0, 0)
                    .setScale(25)
                    .setHeadRotation(225)
                    .render();

            RenderUtils2D.drawRectangle(new Rectangle(x + width - 120 - 2, y + height - 12, 120 * target.getHealth() / target.getMaxHealth(), 10), healthColor(target));
            RenderUtils2D.drawRectangleOutline(new Rectangle(x + width - 120 - 2, y + height - 12, 120, 10), 1, Color.white);

            float health = round(10 * target.getHealth() / (target.getMaxHealth() + target.getAbsorptionAmount()) / 10 * 100);
            customFontManager.drawString("Health", x + width - 120, y + height - 12, Color.white.getRGB());
            customFontManager.drawString(String.valueOf(health), x + width - customFontManager.getWidth(String.valueOf(health)) - 2 - 2, y + height - 12, Color.white.getRGB());
            int startWidth = x + width - 120;
            for (ItemStack itemStack : mc.player.inventory.armorInventory) {
                GlStateManager.enableDepth();
                mc.getRenderItem().zLevel = 200F;
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, startWidth, y + height - 2 - 10 - 7 - 12);
                mc.getRenderItem().renderItemOverlays(mc.fontRenderer,itemStack, startWidth, y + height - 2 - 10 - 7 + 2 - 12);
                mc.getRenderItem().zLevel = 0F;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                startWidth += 15;
            }
        }
    }

    public Color healthColor(EntityLivingBase entity){
        return new HSLColor(90 * entity.getHealth()/entity.getMaxHealth(), 50, 50).toRGB();
    }
}
