package com.firework.client.Implementations.Mixins.MixinsList.Render;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.RenderEntityModelEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.firework.beet.Event;

import java.awt.*;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
        extends Render<T> {

    @Shadow
    public ModelBase mainModel;

    @Shadow protected abstract void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor);

    public MixinRenderLivingBase(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    public void renderModelTail(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo info) {
        RenderEntityModelEvent event = new RenderEntityModelEvent(Event.Stage.PRE, entity,
                render -> {
                    mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                });
        Firework.eventBus.post(event);
        if(event.isCancelled())
            info.cancel();
    }
}