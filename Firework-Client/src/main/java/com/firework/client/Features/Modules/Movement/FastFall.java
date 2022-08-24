package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Entity.LivingUpdateEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.network.play.client.CPacketEntityAction;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "FastFall",
        category = Module.Category.MOVEMENT,
        description = "Helps you to fall faster"
)
public class FastFall extends Module {

    public Setting<Integer> ticks = new Setting<>("Ticks", 25, this, 0, 50);
    public Setting<Integer> delay = new Setting<>("Delay", 500, this, 0, 1000);


    boolean timer;
    boolean sneaking;

    Timer timer1 = new Timer();
    @Override
    public void onEnable() {
        super.onEnable();
        timer = false;
        sneaking = false;
        timer1.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer1.reset();
        if ((timer || sneaking) && mc.player.onGround) {
            timer = false;
            sneaking = false;
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    @Subscribe
    public Listener<LivingUpdateEvent> onUpdate = new Listener<>(event -> {
        if (!event.getEntityLivingBase().equals(mc.player)) return;

        if ((timer || sneaking) && mc.player.onGround) {
            timer = false;
            sneaking = false;
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        if(fullNullCheck()) return;

        if (!mc.player.onGround && mc.player.motionY < 0 && !mc.player.movementInput.jump && !sneaking && timer1.hasPassedMs(delay.getValue())) {
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f - ticks.getValue());
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
            timer = true;
            timer1.reset();
        }
    });
}
