package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Player.PlayerMoveEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Mixins.MixinsList.IEntityPlayerSP;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.MathHelper;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;


@ModuleManifest(
        name = "Speed",
        category = Module.Category.MOVEMENT,
        description = "Makes you faster"
)
public class Speed extends Module{
    private Timer timer = new Timer();

    public Setting<modes> mode = new Setting<>("Mode", modes.Strafe, this);
    public enum modes{
        Strafe
    }
    public Setting<Double> strafeMultiplier = new Setting<>("StrafeMultiplier", 5.2d, this, 0, 20).setVisibility(v-> mode.getValue(modes.Strafe));
    public Setting<Double> strafeMultiplierV2 = new Setting<>("StrafeMultiplierV2", 5.2d, this, 0, 20).setVisibility(v-> mode.getValue(modes.Strafe));
    public Setting<Boolean> boost = new Setting<>("Boost", true, this).setVisibility(v-> mode.getValue(modes.Strafe));
    public Setting<Double> ticks = new Setting<>("Ticks", 4.3d, this, 0, 50).setVisibility(v-> boost.getValue() && mode.getValue(modes.Strafe));
    public Setting<Boolean> forceGround = new Setting<>("ForceGround", true, this).setVisibility(v-> mode.getValue(modes.Strafe));

    float defaultTimerTicks;
    @Override
    public void onEnable(){
        super.onEnable();
        defaultTimerTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
        if(fullNullCheck()) onDisable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.timer.reset();

        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks);
    }

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
       if(event.getPacket() instanceof CPacketPlayer && forceGround.getValue()){
           ((ICPacketPlayer)event.getPacket()).setOnGround(true);
       }
    });

    @Subscribe
    public Listener<PlayerMoveEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if(mode.getValue(modes.Strafe)){
            if (mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) {
                event.x = 0;
                event.z = 0;
            }else if (mc.player.onGround && ((IEntityPlayerSP)mc.player).getPrevOnGround()) {
                if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                    //Jump code
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

                    double[] dir = MathUtil.directionSpeed(strafeMultiplier.getValue()/20);
                    event.x = (mc.player.motionX = dir[0]);
                    event.z = (mc.player.motionZ = dir[1]);
                    event.setCancelled(true);
                }
            } else {
                double[] dir = MathUtil.directionSpeed(strafeMultiplierV2.getValue()/20);
                event.x = (mc.player.motionX = dir[0]);
                event.z = (mc.player.motionZ = dir[1]);
                event.setCancelled(true);
            }
        }
    });
}