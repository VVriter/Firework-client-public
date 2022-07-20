package com.firework.client.Implementations.Mixins.MixinsList;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface IEntity {
    @Accessor(value = "isInWeb")
    boolean isInWeb();

    @Accessor(value = "inPortal")
    void setInPortal(boolean inPortal);

    @Accessor(value = "isImmuneToFire")
    void setImmuneToFire(boolean isImmuneToFire);
}