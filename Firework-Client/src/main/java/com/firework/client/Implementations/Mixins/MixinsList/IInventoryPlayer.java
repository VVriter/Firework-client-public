package com.firework.client.Implementations.Mixins.MixinsList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InventoryPlayer.class)
public interface IInventoryPlayer {

    @Accessor("armorInventory")
    void setArmorInventory(NonNullList<ItemStack> list);
}
