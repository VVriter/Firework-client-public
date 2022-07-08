package com.firework.client.Implementations.Mixins.MixinsList.Interfaces;

import net.minecraft.item.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemTool.class)
public interface IItemTool {
    @Accessor(value = "attackDamage")
    float getAttackDamage();
}
