package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Movement.PushedByShulkerEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Movement.PlayerPushOutOfBlocksEvent;
import com.firework.client.Implementations.Events.Movement.PlayerPushedByWaterEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ISPacketEntityVelocity;
import com.firework.client.Implementations.Mixins.MixinsList.ISPacketExplosion;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Velocity",
        category = Module.Category.COMBAT,
        description = "Anti knock back"
)
public class Velocity extends Module {

    public Setting<Boolean> velocityPacket = new Setting<>("KnockBack", true, this);
    public Setting<Boolean> explosions = new Setting<>("Explosions", true, this);
    public Setting<Boolean> noPush = new Setting<>("NoPush", true, this);

    public Setting<Boolean> shulkers = new Setting<>("Shulkers", true, this);

    public Setting<Double> horizontal = new Setting<>("Horizontal", (double)0, this, -0.3, 100);
    public Setting<Double> vertical = new Setting<>("Vertical", (double)0, this, -0.3, 100);


    @Subscribe
    public Listener<PacketEvent.Receive> onPacket = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketEntityVelocity && velocityPacket.getValue()) {
            // Check it is for us
            if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                // We can just cancel the packet if both horizontal and vertical are 0
                if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                    event.setCancelled(true);
                }
                // Otherwise, we want to modify the values
                else {
                    ((ISPacketEntityVelocity) event.getPacket()).setMotionX((int) ((((SPacketEntityVelocity) event.getPacket()).getMotionX() / 100) * (horizontal.getValue() / 100)));
                    ((ISPacketEntityVelocity) event.getPacket()).setMotionY(vertical.getValue().intValue() / 100);
                    ((ISPacketEntityVelocity) event.getPacket()).setMotionZ((int) ((((SPacketEntityVelocity) event.getPacket()).getMotionZ() / 100) * (horizontal.getValue() / 100)));
                }
            }
        }

        if (event.getPacket() instanceof SPacketExplosion && explosions.getValue()) {
            // We can just cancel the packet if both horizontal and vertical are 0
            if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                event.setCancelled(true);
            }
            // Otherwise, we want to modify the values
            else {
                ((ISPacketExplosion) event.getPacket()).setMotionX((horizontal.getValue().floatValue() / 100) * (((SPacketExplosion) event.getPacket()).getMotionX()));
                ((ISPacketExplosion) event.getPacket()).setMotionY((vertical.getValue().floatValue() / 100) * (((SPacketExplosion) event.getPacket()).getMotionY()));
                ((ISPacketExplosion) event.getPacket()).setMotionZ((horizontal.getValue().floatValue() / 100) * (((SPacketExplosion) event.getPacket()).getMotionZ()));
            }
        }
    });

    @Subscribe
    public Listener<PlayerPushOutOfBlocksEvent> onPush = new Listener<>(e -> {
        e.setCancelled(noPush.getValue());
        }
    );

    @Subscribe
    public Listener<PlayerPushedByWaterEvent> onPushByWater = new Listener<>(e -> {
        e.setCancelled(noPush.getValue());
        }
    );

    @Subscribe
    public Listener<PushedByShulkerEvent> shulker = new Listener<>(e-> {
        e.setCancelled(shulkers.getValue());
    });
}

