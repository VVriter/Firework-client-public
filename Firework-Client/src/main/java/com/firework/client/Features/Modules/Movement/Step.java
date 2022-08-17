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
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.*;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.HashMap;
import java.util.Map;

@ModuleManifest(
        name = "Step",
        category = Module.Category.MOVEMENT,
        description = "Allows you to travel up on blocks"
)
public class Step extends Module {

    public Setting<modes> mode = new Setting<>("Mode", modes.NCP, this);
    public enum modes{
        NCP
    }
    public Setting<Boolean> inhibit = new Setting<>("Inhibit", true, this).setVisibility(()-> mode.getValue(modes.NCP));
    public Setting<Integer> ticks = new Setting<>("Ticks", 25, this, 0, 50).setVisibility(()-> mode.getValue(modes.NCP) && inhibit.getValue());

    public Setting<Double> maxHeight = new Setting<>("MaxHeight", 1d, this, 1, 2).setVisibility(()-> mode.getValue(modes.NCP));

    boolean autoJump;

    boolean timer = false;

    protected final Map<Double, double[]> NCP_OFFSETS = new HashMap<Double, double[]>() {{
        put(0.875, new double[] { 0.39, 0.7, 0.875 });
        put(1.0, new double[] { 0.42, 0.75, 1.0 });
        put(1.5, new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 });
        put(2.0, new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919 });
    }};

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) super.onDisable();
        autoJump = mc.gameSettings.autoJump;
        mc.gameSettings.autoJump = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.autoJump = autoJump;
        if (timer && mode.getValue(modes.NCP)) {
            timer = false;
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
        }
    }

    @Subscribe
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(event -> {
        if(!(event.getPacket() instanceof SPacketPlayerPosLook)) return;
        ((IEntityPlayerSP)mc.player).setLastReportedPosX(mc.player.posX);
        ((IEntityPlayerSP)mc.player).setLastReportedPosY(mc.player.posY);
        ((IEntityPlayerSP)mc.player).setLastReportedPosZ(mc.player.posZ);
    });

    @Subscribe
    public Listener<LivingUpdateEvent> onUpdate = new Listener<>(event -> {
        if (event.getEntityLivingBase().equals(mc.player)) {

            if (timer && mc.player.onGround && mode.getValue(modes.NCP)) {
                timer = false;
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
            }
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if (mode.getValue(modes.NCP) && canStep(getHeight())) {
            if (inhibit.getValue()) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50 + ticks.getValue());
                timer = true;
            }
            double[] offsets = NCP_OFFSETS.getOrDefault(getHeight(), null);
            if(offsets == null) return;

            for(double offset : offsets)
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, mc.player.onGround));

            mc.player.setPosition(mc.player.posX, mc.player.posY + offsets[offsets.length - 1], mc.player.posZ);
            event.setCancelled(true);
        }
    });

    public boolean canStep(double height){
        if(height > maxHeight.getValue()) return false;
        AxisAlignedBB box = mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0);
        return mc.world.getCollisionBoxes(mc.player, box.offset(0.0, height, 0.0)).isEmpty()
                && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isInLava()
                && mc.player.onGround && ((IEntityPlayerSP)mc.player).getPrevOnGround()
                && mc.player.collidedHorizontally;
    }

    public double getHeight() {
        double maxY = 0;
        final AxisAlignedBB grow = mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0).grow(0.05);
        for (AxisAlignedBB aabb : mc.world.getCollisionBoxes(mc.player, grow)) {
            if (aabb.maxY > maxY) {
                maxY = aabb.maxY;
            }
        }
        return maxY - mc.player.posY;
    }
}
