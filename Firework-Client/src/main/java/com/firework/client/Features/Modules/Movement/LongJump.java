package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.IKeyBinding;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;

@ModuleManifest(name = "LongJump",category = Module.Category.MOVEMENT)
public class LongJump extends Module {

    Timer timer = new Timer();

    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();

        if (mc.player.onGround) {
            mc.player.jump();
        }
    }
    @Override
    public void onTick() {
        super.onTick();

    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer.reset();
    }
}
