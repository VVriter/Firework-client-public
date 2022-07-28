package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "EntityFly",
        category = Module.Category.MOVEMENT,
        description = "Fly using entities"
)
public class BoatFly extends Module {
    public Setting<Boolean> yawFix = new Setting<>("YawFix", true, this);
    public Setting<YFix> yFix = new Setting<>("Y", YFix.NoGravity, this);
    public enum YFix{
        NoGravity, Strict, None
    }

    public Setting<Double> horizontal = new Setting<>("Horizontal", (double)20, this, 1, 50);
    public Setting<Double> vertical = new Setting<>("Vertical", (double)20, this, 1, 50);

    Entity ridingEntity;

   @Override
    public void onTick() {
       super.onTick();
        if (mc.player.getRidingEntity() != null) {
            ridingEntity = mc.player.getRidingEntity();
            if (yawFix.getValue()) {
                ridingEntity.rotationYaw = mc.player.rotationYaw;
            }

            if (!yFix.getValue(YFix.NoGravity) && ridingEntity.hasNoGravity()) {
                ridingEntity.setNoGravity(false);
            }


            double[] calc = MathUtil.directionSpeed(horizontal.getValue() / 10);
            ridingEntity.motionX = calc[0];
            ridingEntity.motionZ = calc[1];

        }
    }

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

    @Subscribe
    public Listener<PacketEvent.Send> listener3 = new Listener<>(e-> {
        if ((e.getPacket() instanceof CPacketPlayer.Rotation || e.getPacket() instanceof CPacketInput) && mc.player.isRiding()) {
            e.setCancelled(true);
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        if (ridingEntity != null) {
            ridingEntity.setNoGravity(false);
        }
    }
}
