package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;

public class AirJump extends Module {

    public AirJump() {
        super("AirJump", Category.MOVEMENT);
    }

    @Override
    public void tryToExecute() {
        super.tryToExecute();

        Firework.minecraft.player.onGround = true;
    }
}
