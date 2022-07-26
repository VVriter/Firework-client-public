package com.firework.client.Implementations.Events;

import com.google.common.base.Preconditions;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import ua.firework.beet.Event;

import javax.annotation.Nonnegative;
import java.util.List;

public class OnFishingEvent extends Event {
    private final NonNullList<ItemStack> stacks = NonNullList.create();
    private final EntityFishHook hook;
    private int rodDamage;

    public OnFishingEvent(List<ItemStack> stacks, int rodDamage, EntityFishHook hook)
    {
        this.stacks.addAll(stacks);
        this.rodDamage = rodDamage;
        this.hook = hook;
    }

    /**
     * Get the damage the rod will take.
     * @return The damage the rod will take
     */
    public int getRodDamage()
    {
        return rodDamage;
    }

    /**
     * Specifies the amount of damage that the fishing rod should take.
     * This is not added to the pre-existing damage to be taken.
     * @param rodDamage The damage the rod will take. Must be nonnegative
     */
    public void damageRodBy(@Nonnegative int rodDamage)
    {
        Preconditions.checkArgument(rodDamage >= 0);
        this.rodDamage = rodDamage;
    }

    /**
     * Use this to get the items the player will receive.
     * You cannot use this to modify the drops the player will get.
     * If you want to affect the loot, you should use LootTables.
     */
    public NonNullList<ItemStack> getDrops()
    {
        return stacks;
    }

    /**
     * Use this to stuff related to the hook itself, like the position of the bobber.
     */
    public EntityFishHook getHookEntity()
    {
        return hook;
    }
}
