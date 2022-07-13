package com.firework.client.Implementations.Utill;

import net.minecraft.item.ItemStack;

public class ArmorUtils {

    public static float calculatePercentage(ItemStack stack) {
        float durability = stack.getMaxDamage() - stack.getItemDamage();
        return (durability / (float) stack.getMaxDamage()) * 100F;
    }
}