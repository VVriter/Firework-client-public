package com.firework.client.Implementations.Mixins.MixinsList.Misc;

import com.firework.client.Features.Modules.World.MultiTask;
import com.firework.client.Firework;
import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Mixins.MixinsList.IPlayerControllerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public EntityPlayerSP player;

    @Shadow
    public PlayerControllerMP playerController;

    @Shadow
    public GameSettings gameSettings;

    private boolean handActive = false;
    private boolean isHittingBlock = false;

    @Shadow(aliases = "func_147116_af") 
    protected abstract void clickMouse();

    @Inject(method = "processKeyBinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z", shift = At.Shift.BEFORE, ordinal = 2))
    public void processKeyBindsInvokeIsKeyDown(CallbackInfo ci) {
        if (Firework.moduleManager.getModuleByClass(MultiTask.class).isEnabled.getValue()) {
            while (this.gameSettings.keyBindAttack.isPressed()) {
                this.clickMouse();
            }
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"))
    public void rightClickMousePre(CallbackInfo ci) {
        if (Firework.moduleManager.getModuleByClass(MultiTask.class).isEnabled.getValue()) {
            isHittingBlock = playerController.getIsHittingBlock();
            ((IPlayerControllerMP) playerController).setHitting(false);
        }
    }

    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    public void rightClickMousePost(CallbackInfo ci) {
        if (Firework.moduleManager.getModuleByClass(MultiTask.class).isEnabled.getValue() && !playerController.getIsHittingBlock()) {
            ((IPlayerControllerMP) playerController).setHitting(isHittingBlock);
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"))
    public void sendClickBlockToControllerPre(boolean leftClick, CallbackInfo ci) {
        if (Firework.moduleManager.getModuleByClass(MultiTask.class).isEnabled.getValue()) {
            handActive = player.isHandActive();
            ((IEntityPlayerSP) player).kbSetHandActive(false);
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("RETURN"))
    public void sendClickBlockToControllerPost(boolean leftClick, CallbackInfo ci) {
        if (Firework.moduleManager.getModuleByClass(MultiTask.class).isEnabled.getValue() && !player.isHandActive()) {
            ((IEntityPlayerSP) player).kbSetHandActive(handActive);
        }
    }
}
