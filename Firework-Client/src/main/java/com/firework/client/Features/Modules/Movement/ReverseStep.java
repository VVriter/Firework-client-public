package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Entity.LivingUpdateEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "ReverseStep",
        category = Module.Category.MOVEMENT,
        description = ""
)
public class ReverseStep extends Module {

    public Setting<Integer> distance = new Setting<>("FallDistance", 3, this, 1, 5);
    public Setting<Integer> ticks = new Setting<>("Ticks", 25, this, 0, 50);

    boolean timer;

    @Override
    public void onEnable() {
        super.onEnable();
        timer = false;
    }

    @Subscribe
    public Listener<LivingUpdateEvent> onUpdate = new Listener<>(event -> {
        if (!event.getEntityLivingBase().equals(mc.player)) return;

        if (timer && mc.player.onGround) {
            timer = false;
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        if (!canStep()) return;

        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50 + ticks.getValue());
        mc.player.motionY = -1;
    });


    boolean canStep() {
        AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        return mc.world.getCollisionBoxes(mc.player,bb.offset(0,-1,0)).isEmpty()
                && !mc.player.isInLava() && !mc.player.isInWater() && !mc.player.isOnLadder()
                && ((IEntityPlayerSP)mc.player).getPrevOnGround() && !mc.player.movementInput.jump;
    }
}
