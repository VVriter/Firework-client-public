package com.firework.client.Implementations.Mixins.MixinsList;
import net.minecraft.entity.passive.EntityPig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.datasync.DataParameter;

@Mixin(EntityPig.class)
public interface IPig {
    @Accessor(value = "SADDLED")
    DataParameter<Boolean> isSaddeled();

    @Accessor(value = "SADDLED")
    void setSaddled(DataParameter<Boolean> value);
}