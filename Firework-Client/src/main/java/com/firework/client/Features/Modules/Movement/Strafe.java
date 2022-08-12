package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Player.PlayerMoveEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.MathHelper;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Objects;


@ModuleManifest(
        name = "Strafe",
        category = Module.Category.MOVEMENT,
        description = "Makes you faster"
)
public class Strafe extends Module{

    public Setting<modes> mode = new Setting<>("Mode", modes.Strafe, this);
    public enum modes{
        Strafe
    }
    public Setting<Double> strafeMultiplier = new Setting<>("Multiplier", 12.3d, this, 0, 20).setVisibility(v-> mode.getValue(modes.Strafe));
    public Setting<Double> scale = new Setting<>("Scale", 5.2d, this, 0, 20).setVisibility(v-> mode.getValue(modes.Strafe));
    public Setting<Boolean> forceGround = new Setting<>("ForceGround", true, this).setVisibility(v-> mode.getValue(modes.Strafe));

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
       if(event.getPacket() instanceof CPacketPlayer && forceGround.getValue()){
           ((ICPacketPlayer)event.getPacket()).setOnGround(true);
       }
    });

    @Subscribe
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook && mode.getValue(modes.Strafe)) {
            ((IEntityPlayerSP)mc.player).setLastReportedPosX(mc.player.posX);
            ((IEntityPlayerSP)mc.player).setLastReportedPosY(mc.player.posY);
            ((IEntityPlayerSP)mc.player).setLastReportedPosZ(mc.player.posZ);
        }
    });

    @Subscribe
    public Listener<PlayerMoveEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if(mode.getValue(modes.Strafe)){
            if (mc.player.onGround && ((IEntityPlayerSP)mc.player).getPrevOnGround()){
                if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                    event.y = (mc.player.motionY = 0.42);

                    if (mc.player.isPotionActive(MobEffects.JUMP_BOOST))
                        event.y = (mc.player.motionY += (float)(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);

                    if (mc.player.isSprinting()) {
                        float f = mc.player.rotationYaw * 0.017453292F;
                        event.x = (mc.player.motionX -= MathHelper.sin(f) * 0.2F);
                        event.z  += MathHelper.cos(f) * 0.2F;
                    }

                    mc.player.isAirBorne = true;
                    net.minecraftforge.common.ForgeHooks.onLivingJump(mc.player);
                    mc.player.addStat(StatList.JUMP);

                    if (mc.player.isSprinting())
                        mc.player.addExhaustion(0.2F);
                    else
                        mc.player.addExhaustion(0.05F);

                    double[] dir = MathUtil.directionSpeed(strafeMultiplier.getValue()/20, scale.getValue().floatValue());
                    event.x = (mc.player.motionX = dir[0]);
                    event.z = (mc.player.motionZ = dir[1]);
                    event.setCancelled(true);
                }
            }
        } else {
            double[] dir2 = MathUtil.directionSpeed((strafeMultiplier.getValue()-3.2)/20, scale.getValue().floatValue());

            event.x = (mc.player.motionX = dir2[0]);
            event.z = (mc.player.motionZ = dir2[1]);
            event.setCancelled(true);
        }
    });
}