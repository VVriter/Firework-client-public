package com.firework.client.Implementations.Mixins.MixinsList.Blocks;

import com.firework.client.Features.Modules.Movement.NoSlow;
import net.minecraft.block.BlockSlime;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSlime.class)
public class MixinBlockSlime {
    @Inject(method = "onEntityWalk", at = @At("HEAD"), cancellable = true)
    public void onEntityCollide(World worldIn, BlockPos pos, Entity entityIn, CallbackInfo ci) {
        try {
            if (NoSlow.slimes.getValue() && NoSlow.enabled.getValue()) {
                ci.cancel();
            }
        }
        // Why does this throw an NPE
        catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }
}
