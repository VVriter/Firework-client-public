package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.Entity;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "BoatFly",
        category = Module.Category.MOVEMENT
)
public class BoatFly extends Module {

    public Setting<YFix> yFix = new Setting<>("Y", YFix.NoGravity, this);
    public enum YFix{
        NoGravity, Strict, None
    }

    public Setting<Boolean> yawFix = new Setting<>("YawFix", true, this);

    Entity ridingEntity;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (mc.player.getRidingEntity() != null) {
            ridingEntity = mc.player.getRidingEntity();
            if (yawFix.getValue()) {
                ridingEntity.rotationYaw = mc.player.rotationYaw;
            }
        }

        if (!yFix.getValue(YFix.NoGravity) && ridingEntity.hasNoGravity()) {
            ridingEntity.setNoGravity(false);
        }
    });

    @Override
    public void onUpdate() {
        super.onUpdate();
        switch (yFix.getValue()) {
            case NoGravity:
                ridingEntity.setNoGravity(true);
                break;
            case Strict:
                //Strict code
                break;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (ridingEntity != null) {
            ridingEntity.setNoGravity(false);
        }
    }
}
