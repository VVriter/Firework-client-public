package com.firework.client.Implementations.Mixins.MixinsList.Misc;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Entity.EntityMoveEvent;
import com.firework.client.Implementations.Events.Entity.EntityPushEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
    public float stepHeight;

    @Shadow
    private AxisAlignedBB boundingBox;

    @Inject(method = "applyEntityCollision", at = @At(value = "HEAD"))
    public void onEntityCollision(Entity entityIn, CallbackInfo ci) {
        EntityPushEvent event = new EntityPushEvent(entityIn);

        Firework.eventBus.post(event);

        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", shift = At.Shift.BEFORE, ordinal = 0))
    public void onMove(MoverType type, double x, double y, double z, CallbackInfo info) {
        EntityMoveEvent moveEvent = new EntityMoveEvent(boundingBox, (Entity) (Object) this, 0.5f);
        Firework.eventBus.post(moveEvent);
    }
}
