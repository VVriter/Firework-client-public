package com.firework.client.Features.CommandsSystem.Commands.PeekCommand;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;
import sun.audio.AudioPlayer;

public class ToolTips {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void displayInv(ItemStack stack, String name) {
        try {
            Item item = stack.getItem();
            TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            ItemShulkerBox shulker = (ItemShulkerBox) item;
            shulker.getBlock();
            entityBox.setWorld(ToolTips.mc.world);
            entityBox.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
            entityBox.setCustomName(name == null ? stack.getDisplayName() : name);
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                ToolTips.mc.player.displayGUIChest(entityBox);
            }).start();
        } catch (Exception exception) {
            // empty catch block
        }
    }
}
