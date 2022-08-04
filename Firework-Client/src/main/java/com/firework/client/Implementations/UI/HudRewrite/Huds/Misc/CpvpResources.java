package com.firework.client.Implementations.UI.HudRewrite.Huds.Misc;

import com.firework.client.Implementations.UI.HudRewrite.Huds.HudComponent;
import com.firework.client.Implementations.UI.HudRewrite.Huds.HudManifest;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;

@HudManifest(name = "CpvpResources")
public class CpvpResources extends HudComponent {

    @Override
    public void load() {
        super.load();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        x = scaledResolution.getScaledWidth()/2;
        y = scaledResolution.getScaledHeight()/2;
        width = 16;
    }

    public int getCount(Item item){
        return mc.player.inventory.mainInventory
                .stream().filter(itemStack -> itemStack.getItem() == item)
                .mapToInt(ItemStack::getCount).sum();
    }

    @Override
    public void onRender() {
        super.onRender();
        Item[] items = new Item[]{
                Items.END_CRYSTAL, Items.GOLDEN_APPLE, Items.TOTEM_OF_UNDYING, Item.getItemFromBlock(Blocks.OBSIDIAN)
        };

        ArrayList<Item> availableItemStacks = new ArrayList<>();

        for(Item item : items){
            if(item != null && !InventoryUtil.hasItem(item)) continue;
            availableItemStacks.add(item);
        }

        height = availableItemStacks.size() * 17;

        for(int i = 0; i < availableItemStacks.size(); i++){
            ItemStack itemStack = new ItemStack(availableItemStacks.get(i));
            RenderUtils2D.renderItemStack(itemStack, new Point(x, y + i*17), String.valueOf(getCount(itemStack.getItem())));
        }
    }
}
