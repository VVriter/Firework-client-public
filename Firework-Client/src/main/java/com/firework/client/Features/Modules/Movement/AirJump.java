package com.firework.client.Features.Modules.Movement;


import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

public class AirJump extends Module {
    public int delay = 1000;

    public static Setting<Boolean> enabled = null;
    public AirJump() {
        super("AirJump", Category.MOVEMENT);
        enabled = this.isEnabled;
    }

    @Override
    public void onTick() {
        super.onTick();
        mc.player.onGround = true;
    }
}