package com.firework.client.Implementations.UI.HudRewrite.Huds.Misc;

import com.firework.client.Firework;
import com.firework.client.Implementations.UI.HudRewrite.Huds.HudComponent;
import com.firework.client.Implementations.UI.HudRewrite.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;

@HudManifest(name = "InventoryPreview")
public class InventoryPreview extends HudComponent {

    @Override
    public void load() {
        super.load();
        this.x = 500;
        this.y = 80;
        this.height = 3 * 16;
        this.width = 9 * 16;
    }

    @Override
    public void onRender() {
        super.onRender();
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), new Color(1, 1, 1, 107));
        RenderUtils2D.drawRectangleOutline(new Rectangle(x-1, y-1, width+2, height+2), 1, Firework.colorManager.getColor());
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 10; i < 36; i++) {
            ItemStack itemStack = mc.player.inventoryContainer.getInventory().get(i);
            int offsetX = (i % 9) * 16;
            int offsetY = ((i / 9) - 1) * 16;
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x + offsetX, y + offsetY);
            renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x + offsetX, y + offsetY, null);
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
        if (!stack.isEmpty()){
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? String.valueOf(stack.getCount()) : text;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                Firework.customFontManagerInv.drawStringWithShadow(s, (float)(xPosition + 19 - fr.getStringWidth(s)), (float)(yPosition + 6 + 3), 16777215);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
            }

            if (stack.getItem().showDurabilityBar(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
                int i = Math.round(13.0F - (float)health * 13.0F);
                int j = rgbfordisplay;
                RenderUtils2D.drawRectangle(new Rectangle(xPosition + 2, yPosition + 13, 13, 2), new Color(0, 0, 0));
                RenderUtils2D.drawRectangle(new Rectangle(xPosition + 2, yPosition + 13, i, 1), new Color(j >> 16 & 255, j >> 8 & 255, j & 255));
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }
}
