package com.firework.client.Implementations.Mixins.MixinsList;

import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityArrow.class)
public interface IEntityArrow {

    @Accessor("inGround")
    boolean isInGround();

}