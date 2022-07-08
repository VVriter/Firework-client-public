package com.firework.client.Implementations.Mixins.MixinsList.Render;


import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Render.class})
public class MixinRenderer<T extends Entity> {
    @Shadow
    protected boolean renderOutlines;
    @Shadow
    @Final
    protected RenderManager renderManager;

    MixinRenderer() {
    }

    @Shadow
    protected  boolean bindEntityTexture(T var1){return true;}

    @Shadow
    protected  int getTeamColor(T var1){return 0;}
}