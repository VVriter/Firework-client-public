package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.Interfaces.IKeyBinding;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;

@ModuleManifest(name = "LongJump",category = Module.Category.MOVEMENT)
public class LongJump extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Acc, this, modes.values());
    public enum modes{
       Acc
    }

    public Setting<Boolean> autoDisable = new Setting<>("autoDisable", true, this).setVisibility(mode.getValue(modes.Acc));

    Timer startTimer = new Timer();

    @Override
    public void onEnable() {
        super.onEnable();
        startTimer.reset();
        if (mode.getValue(modes.Acc)) {
            if (mc.player.onGround) {
                mc.player.jump();
            }
            ((IKeyBinding)mc.gameSettings.keyBindForward).setPressed(true);
        }
    }
    @Override
    public void onTick() {
        super.onTick();
        if (mode.getValue(modes.Acc)) {
            if (mc.player.motionY == 0.0030162615090425808D)
                mc.player.jumpMovementFactor = 1.0F;
            if (startTimer.hasPassedMs(2000)) {
                if (autoDisable.getValue()) {
                    onDisable();
                }
                startTimer.reset();
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        startTimer.reset();
        if (mode.getValue(modes.Acc)) {
            ((IKeyBinding) mc.gameSettings.keyBindForward).setPressed(false);
        }
    }
}
