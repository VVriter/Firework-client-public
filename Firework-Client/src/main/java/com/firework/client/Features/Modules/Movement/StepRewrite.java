package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Mixins.MixinsList.IKeyBinding;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Timer;
import com.sun.jna.platform.win32.Winspool;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "StepRewrite", category = Module.Category.MOVEMENT)
public class StepRewrite extends Module {
    private Setting<Boolean> forceGroundPacket = new Setting<>("ForceGround", true, this);
    private Setting<Double> vanillaSpeed = new Setting<>("Speed", 2.1d, this, 1, 20);

    private Setting<Double> boostTimer = new Setting<>("FWBoostDelayMs", 29.8d, this, 1, 200);
    private Setting<Double> jumpTimerDelay = new Setting<>("JumpDelayMs", 185.6d, this, 1, 200);

    private boolean oldAutoJump = false;

    private Timer fowardBoostTimer;
    private Timer jumpTimer;

    @Override
    public void onEnable() {
        super.onEnable();
        fowardBoostTimer = new Timer();
        fowardBoostTimer.reset();

        jumpTimer = new Timer();
        jumpTimer.reset();

        oldAutoJump = mc.gameSettings.autoJump;
        mc.gameSettings.autoJump = false;
    }

    @Override
    public void onTick() {
        super.onTick();
        if(mc.player.collidedHorizontally)
            if(jumpTimer.hasPassedMs(jumpTimerDelay.getValue())) {
                mc.player.jump();
                jumpTimer.reset();
            }

        if(mc.player.motionY > 0) {
            ((IKeyBinding) mc.gameSettings.keyBindSneak).setPressed(true);
        }else if(mc.player.motionY < 0){
            if(fowardBoostTimer.hasPassedMs(boostTimer.getValue())) {
                ((IKeyBinding) mc.gameSettings.keyBindSneak).setPressed(false);

                double[] calc = MathUtil.directionSpeed(this.vanillaSpeed.getValue() / 10.0);
                mc.player.motionX = calc[0];
                mc.player.motionZ = calc[1];
                fowardBoostTimer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && forceGroundPacket.getValue()) {
            ((ICPacketPlayer) event.getPacket()).setOnGround(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.autoJump = oldAutoJump;
    }
}
