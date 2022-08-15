package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Timer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "LongJump",
        category = Module.Category.MOVEMENT,
        description = "Jumps longer"
)
public class LongJump extends Module {

    public Setting<Double> speed = new Setting<>("Speed", (double)3, this, 1, 10);
    double y;
    Timer timer = new Timer();

    @Override
    public void onEnable() {
        super.onEnable();
        y = 0.01;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(e-> {
        if (mc.gameSettings.keyBindForward.isKeyDown()) {
            double[] calc = MathUtil.directionSpeed(speed.getValue() / 10);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
            mc.player.motionY = y;
        }

        if (y == 0.1 && timer.hasPassedMs(1500)) {
            y = -0.01;
            timer.reset();
        }

        if (mc.player.onGround) {
            y = 0.01;
        }
    });
}
