package com.firework.client.Implementations.Mixins.MixinsList.Blocks;

import com.firework.client.Features.Modules.Movement.NoSlow;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSoulSand.class)
public class MixinBlockSoulSand {
    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void onEntityCollide(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo ci) {
        try {
                if (NoSlow.soulSand.getValue() && NoSlow.enabled.getValue()) {
                    ci.cancel();
                }
        }
        // Why does this throw an NPE
        catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }
}
