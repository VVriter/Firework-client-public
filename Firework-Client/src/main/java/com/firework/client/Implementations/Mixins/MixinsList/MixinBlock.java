package com.firework.client.Implementations.Mixins.MixinsList;
import com.firework.client.Features.Modules.Render.CaveFinder;
import com.firework.client.Features.Modules.Render.CaveFinder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> info) {
        if (CaveFinder.enabled.getValue() && CaveFinder.WHITELIST.contains((Block) (Object) this)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "getBlockLayer", at = @At("HEAD"), cancellable = true)
    public void getBlockLayer(CallbackInfoReturnable<BlockRenderLayer> info) {
        if (CaveFinder.enabled.getValue() && !CaveFinder.WHITELIST.contains((Block) (Object) this)) {
            info.setReturnValue(BlockRenderLayer.TRANSLUCENT);
        }
    }

    @Inject(method = "getLightValue(Lnet/minecraft/block/state/IBlockState;)I", at = @At("HEAD"), cancellable = true)
    public void getLightValue(IBlockState state, CallbackInfoReturnable<Integer> info) {
        if (CaveFinder.enabled.getValue()) {
            info.setReturnValue(100);
        }
    }
}