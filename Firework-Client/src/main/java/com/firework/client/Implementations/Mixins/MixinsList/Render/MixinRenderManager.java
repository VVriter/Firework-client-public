package com.firework.client.Implementations.Mixins.MixinsList.Render;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.RenderEntityModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityXPOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.firework.beet.Event;

@Mixin(RenderManager.class)
public class MixinRenderManager {

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void renderEntityPre(Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo info) {

        if (entity instanceof EntityEnderPearl || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityEnderCrystal) {
            RenderEntityModelEvent event = new RenderEntityModelEvent(Event.Stage.PRE, entity, render -> {
                Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity).doRender(entity, x, y, z, entity.rotationYaw, partialTicks);
            });
            Firework.eventBus.post(event);

            if (event.isCancelled())
                info.cancel();
        }
    }
}
