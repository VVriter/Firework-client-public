package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;

public class AirJump extends Module {
    public Setting<Float> jumpForce = new Setting<>("JumpForce", 1f, this, 1f, 30f);

    public AirJump() {
        super("AirJump", Category.MOVEMENT);
    }

    @Override
    public void tryToExecute() {
        super.tryToExecute();

        Firework.minecraft.player.setVelocity(0, jumpForce.getValue(), 0);
    }
}
