package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;

@ModuleArgs(name = "AntiVoid",category = Module.Category.MOVEMENT)
public class AntiVoid extends Module {

    @Override
    public void onTick() {
        super.onTick();
        if (mc.player.posY <= 0.5) {
            mc.player.moveVertical = 10.0f;
            mc.player.jump();
        }
        else {
           mc.player.moveVertical = 0.0f;
        }
    }

    public void onDisable() {
        mc.player.moveVertical = 0.0f;
    }
}
