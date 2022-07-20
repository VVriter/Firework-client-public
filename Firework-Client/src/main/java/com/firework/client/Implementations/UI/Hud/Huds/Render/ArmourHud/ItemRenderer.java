package com.firework.client.Implementations.UI.Hud.Huds.Render.ArmourHud;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

import static com.firework.client.Features.Modules.Module.mc;

public class ItemRenderer {
    public static void renderItemStack(ItemStack itemStack, float x, float y, boolean overlay) {
        RenderItem renderItem = mc.getRenderItem();

        GlStateManager.enableDepth();

        renderItem.zLevel = 200;
        renderItem.renderItemAndEffectIntoGUI(itemStack, (int) x, (int) y);

        if (overlay) {
            renderItem.renderItemOverlays(mc.fontRenderer, itemStack, (int) x, (int) y);
        }

        renderItem.zLevel = 0;

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
    }

}
