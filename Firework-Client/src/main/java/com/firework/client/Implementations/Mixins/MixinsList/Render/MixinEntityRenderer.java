package com.firework.client.Implementations.Mixins.MixinsList.Render;

import com.firework.client.Features.Modules.Misc.CameraClip;
import com.firework.client.Features.Modules.Render.Sky;
import com.firework.client.Features.Modules.Render.NoRender;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Player.TraceEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Mixin(value={EntityRenderer.class})
public abstract class MixinEntityRenderer {
   @Shadow
    private ItemStack itemActivationItem;
    @Shadow
    @Final
    private Minecraft mc;
    private boolean injection = true;

    @Shadow
    public abstract void getMouseOver(float var1);

  /*  @Inject(method={"getFOVModifier"}, at={@At(value="HEAD")}, cancellable=true)
    public void fov(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.fov.getValue()) {
            info.cancel();
        }
    }*/

    @Inject(method={"updateFovModifierHand"}, at={@At(value="HEAD")}, cancellable=true)
    public void fov2(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.fov.getValue()) {
            info.cancel();
        }
    }

 /*   @Inject(method={"renderHand"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderHand(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.hands.getValue()) {
            info.cancel();
        }
    } */

    @Inject(method={"renderRainSnow"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderRainSnow(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.weather.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"renderItemActivation"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemActivationHook(CallbackInfo info) {
        if (this.itemActivationItem != null && NoRender.enabled.getValue() && NoRender.totemPops.getValue().booleanValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }

    @Inject(method={"addRainParticles"}, at={@At(value="HEAD")}, cancellable=true)
    public void addRainParticles(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.weather.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"updateLightmap"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.enabled.getValue() && (NoRender.skylight.getValue())) {
            info.cancel();
        }
    }


  @Inject(method={"setupFog"}, at={@At(value="HEAD")}, cancellable=true)
    public void setupFogHook(int startCoords, float partialTicks, CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.antiFog.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.hurtcam.getValue().booleanValue()) {
            info.cancel();
        }
    }

   @Redirect(method={"getMouseOver"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcludingHook(WorldClient instance, net.minecraft.entity.Entity entity, AxisAlignedBB axisAlignedBB, com.google.common.base.Predicate predicate) {
       TraceEvent event = new TraceEvent();
       Firework.eventBus.post(event);
        if (event.isCancelled()) {
            return new ArrayList<Entity>();
        }
        return instance.getEntitiesInAABBexcluding(entity, axisAlignedBB, predicate);
    }

    @ModifyVariable(method={"orientCamera"}, ordinal=3, at=@At(value="STORE", ordinal=0), require=1)
    public double changeCameraDistanceHook(double range) {
        return CameraClip.enabled.getValue() && CameraClip.extend.getValue() != false ? CameraClip.valX.getValue() : range;
    }

    @ModifyVariable(method={"orientCamera"}, ordinal=7, at=@At(value="STORE", ordinal=0), require=1)
    public double orientCameraHook(double range) {
        return CameraClip.enabled.getValue() && CameraClip.extend.getValue() != false ? CameraClip.valX.getValue() : (CameraClip.enabled.getValue() && CameraClip.extend.getValue() == false ? 4.0 : range);
    }

    @Redirect(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderWorldPass(float y, float ratio, float zNear, float zFar) {
        Project.gluPerspective(y, Sky.enabled.getValue() ? ((float) Sky.worldX.getValue() / (float) Sky.worldY.getValue()) : ratio, zNear, zFar);
    }
}
