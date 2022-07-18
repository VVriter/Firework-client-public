package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

@ModuleManifest(name = "AutoHotbar", category = Module.Category.MISC)
public class AutoHotBar extends Module {

    private int lastSlot = -1;
    private Item lastItem;

    @Override
    public void onTick() {
        super.onTick();
        if (mc.player == null) {
            return;
        }

        Item held = mc.player.getHeldItemMainhand().getItem();
        if (lastSlot == mc.player.inventory.currentItem && lastItem != held && held == Items.AIR && mc.currentScreen == null) {
            if (!mc.gameSettings.keyBindDrop.isKeyDown() || mc.gameSettings.keyBindDrop.isKeyDown() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                int slot = InventoryUtil.getItemSlot(lastItem);
                if (slot != -1) {
                    InventoryUtil.clickSlot(slot);
                    InventoryUtil.clickSlot(mc.player.inventory.currentItem);
                }
            }
        }

        lastSlot = mc.player.inventory.currentItem;
        lastItem = mc.player.getHeldItemMainhand().getItem();
    }
}
