package com.firework.client.Implementations.UI.HudRewrite.Huds.Misc;

import com.firework.client.Firework;
import com.firework.client.Implementations.UI.HudRewrite.Huds.HudComponent;
import com.firework.client.Implementations.UI.HudRewrite.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.item.ItemStack;

import java.awt.*;

@HudManifest(name = "InventoryPreview")
public class InventoryPreview extends HudComponent {

    @Override
    public void load() {
        super.load();
        this.x = 250;
        this.y = 20;
        this.height = 3 * 16;
        this.width = 9 * 16;
    }

    @Override
    public void onRender() {
        super.onRender();
        RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, height), new Color(1, 1, 1, 107));
        RenderUtils2D.drawRectangleOutline(new Rectangle(x-1, y-1, width+2, height+2), 1, Firework.colorManager.getColor());
        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = mc.player.inventoryContainer.getInventory().get(i);
            int offsetX = (i % 9) * 16;
            int offsetY = ((i / 9) - 1) * 16;
            RenderUtils2D.renderItemStack(itemStack,new Point(x + offsetX, y + offsetY));
        }
    }
}
