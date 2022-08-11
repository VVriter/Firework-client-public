package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.ClientTickEvent;
import com.firework.client.Implementations.Events.Entity.EntityMoveEvent;
import com.firework.client.Implementations.Events.Entity.LivingUpdateEvent;
import com.firework.client.Implementations.Events.Movement.InputUpdateEvent;
import com.firework.client.Implementations.Events.Movement.MoveEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Step",
        category = Module.Category.MOVEMENT,
        description = "Allows you to travel up on blocks"
)
public class Step extends Module {

    public Setting<modes> mode = new Setting<>("Mode", modes.Strict, this);
    public enum modes{
        Strict
    }
    public Setting<Boolean> inhibit = new Setting<>("Inhibit", true, this).setVisibility(v-> mode.getValue(modes.Strict));

    boolean autoJump;

    boolean timer = false;

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
    }

    @Subscribe
    public Listener<LivingUpdateEvent> onUpdate = new Listener<>(event -> {
        if (event.getEntityLivingBase().equals(mc.player)) {

            if (timer && mc.player.onGround && mode.getValue(modes.Strict)) {
                timer = false;
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f);
            }
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if (mode.getValue(modes.Strict) && canStep()) {
            if (inhibit.getValue()) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50.0f / (1.0f / 2));
                timer = true;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.75, mc.player.posZ, mc.player.onGround));
            mc.player.setPosition(mc.player.posX, mc.player.posY+1, mc.player.posZ);
        }
    });



    public boolean canStep(){
        AxisAlignedBB box = mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0).grow(0.05);
        return mc.world.getCollisionBoxes(mc.player, box.offset(0.0, 1.0, 0.0)).isEmpty()
                && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isInLava()
                && mc.player.onGround && ((IEntityPlayerSP)mc.player).getPrevOnGround() && mc.player.collidedHorizontally;
    }
}
