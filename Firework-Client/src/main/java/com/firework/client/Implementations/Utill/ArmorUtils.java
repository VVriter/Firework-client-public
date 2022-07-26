package com.firework.client.Implementations.Utill;

import net.minecraft.item.ItemStack;

public class ArmorUtils {

    public static int getPercentageDurability(ItemStack itemStack) {
        return (int)(((double)getDurability(itemStack) / (double)itemStack.getMaxDamage()) * 100);
    }

    public static boolean hasDurability(ItemStack itemStack) {
        return itemStack.getMaxDamage() != 0;
    }

    public static int getDurability(ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getItemDamage();
    }
}