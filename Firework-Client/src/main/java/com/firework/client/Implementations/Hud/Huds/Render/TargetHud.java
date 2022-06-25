package com.firework.client.Implementations.Hud.Huds.Render;

import com.firework.client.Implementations.Hud.HudGui;
import com.firework.client.Implementations.Hud.HudInfo;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Managers.Text.CustomFontRenderer.CustomFontUtil;
import com.firework.client.Implementations.Managers.Text.font.FontUtil;
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


        EntityPlayer target = mc.player;

        RenderUtils2D.drawRectAlpha(new Rectangle(x, y + 12, width, height), HudInfo.fillColorA);
        if(target.isDead){
            mc.getTextureManager().bindTexture(resourceLocation("firework/textures/skull.png"));
            RenderUtils2D.drawCompleteImage(x + 15, y + 15 + 12, height-25, height-25);
        }else {
            int startWidth = x + width - 120;
            for (ItemStack itemStack : mc.player.inventory.armorInventory) {
                GlStateManager.enableDepth();
                mc.getRenderItem().zLevel = 200F;
                mc.getRenderItem().renderItemAndEffectIntoGUI(target, itemStack, startWidth, y + height - 2 - 10 - 7);
                //mc.getRenderItem().renderItemOverlays(mc.fontRenderer,itemStack, startWidth, y + height - 2 - 10 - 7 + 2);
                mc.getRenderItem().zLevel = 0F;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                startWidth += 15;
            }

            GuiInventory.drawEntityOnScreen(x + 30, y + 60, 25, 0, 0, target);
            RenderUtils2D.drawRectangle(new Rectangle(x + width - 120 - 2, y + height - 2, 120 * target.getHealth() / target.getMaxHealth(), 10), healthColor(target));
            RenderUtils2D.drawRectangleOutline(new Rectangle(x + width - 120 - 2, y + height - 2, 120, 10), 1, Color.white);

            float health = round(10 * target.getHealth() / (target.getMaxHealth() + target.getAbsorptionAmount()) / 10 * 100);
            FontUtil.normal.drawString("Health", x + width - 120, y + height - 1, Color.white.getRGB());
            FontUtil.normal.drawString(String.valueOf(health), x + width - customFontManager.getWidth(String.valueOf(health)) - 2 - 2, y + height - 1, Color.white.getRGB());
        }
    }

    public Color healthColor(EntityLivingBase entity){
        return new HSLColor(90 * entity.getHealth()/entity.getMaxHealth(), 50, 50).toRGB();
    }
}
