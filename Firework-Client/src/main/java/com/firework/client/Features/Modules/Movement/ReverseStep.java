package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Entity.LivingUpdateEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "ReverseStep",
        category = Module.Category.MOVEMENT,
        description = "Helps you to fall faster"
)
public class ReverseStep extends Module {

    public Setting<Integer> ticks = new Setting<>("Ticks", 25, this, 0, 50);

    boolean timer;
    boolean sneaking;

    @Override
    public void onEnable() {
        super.onEnable();
        timer = false;
        sneaking = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
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

        if (!mc.player.onGround && mc.player.motionY < 0 && !mc.player.movementInput.jump && !sneaking) {
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f - ticks.getValue());
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
            timer = true;
        }
    });
}
