package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import org.lwjgl.input.Keyboard;

public class AirJump extends Module {
    public int delay = 1000;

    public Setting<Double> jumpForce = new Setting<>("JumpForce", 1d, this, 1, 30);

    public AirJump() {
        super("AirJump", Category.MOVEMENT);
    }

    @Override
    public void tryToExecute() {
        super.tryToExecute();
        if(Keyboard.isKeyDown(Firework.minecraft.gameSettings.keyBindJump.getKeyCode())) {
            Firework.minecraft.player.setVelocity(0, jumpForce.getValue() * Firework.minecraft.getRenderPartialTicks(), 0);
        }
    }
}
