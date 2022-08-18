package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Utill.RotationUtil;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "AntiAim",
        category = Module.Category.MOVEMENT,
        description = "Anti aim like in csgo"
)
public class AntiAim extends Module {

    float yaw;

    @Override
    public void onEnable() {
        super.onEnable();
        yaw = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();

    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ex = new Listener<>(e-> {
        if (yaw >= 180) {
            yaw = -180;
        }

        if (yaw < 180) {
            yaw+=20;
        }

        RotationUtil.packetFacePitchAndYaw(mc.player.rotationPitch, yaw);
        e.setCancelled(true);
    });
}
