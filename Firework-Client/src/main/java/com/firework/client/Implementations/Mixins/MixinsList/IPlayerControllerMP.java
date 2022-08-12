package com.firework.client.Implementations.Mixins.MixinsList;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMP {

    @Accessor("isHittingBlock")
    void setHitting(boolean value);

    @Accessor("blockHitDelay")
    void setBlockHitDelay(int value);
}
