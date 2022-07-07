package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Mixins.MixinsList.IKeyBinding;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Timer;
import com.sun.jna.platform.win32.Winspool;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "StepRewrite", category = Module.Category.MOVEMENT)
public class StepRewrite extends Module {
    private Setting<Boolean> forceGroundPacket = new Setting<>("ForceGround", true, this);
    private Setting<Double> vanillaSpeed = new Setting<>("Speed", 2.1d, this, 1, 20);

    private Setting<Double> boostTimer = new Setting<>("FWBoostDelayMs", 29.8d, this, 1, 200);
    private Setting<Double> jumpTimerDelay = new Setting<>("JumpDelayMs", 185.6d, this, 1, 200);

    private Setting<Boolean> fallDistance = new Setting<>("FallDistance", true, this);

    private boolean oldAutoJump = false;

    private boolean canBoost = false;

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

        canBoost = false;
    }

    @Override
    public void onTick() {
        super.onTick();
        if(mc.player.collidedHorizontally) {
            if(mc.player.fallDistance < 1 && fallDistance.getValue()) {
                BlockPos playerPos = EntityUtil.getFlooredPos(mc.player);
                BlockPos pos1 = playerPos.offset(mc.player.getHorizontalFacing());
                if (BlockUtil.getBlock(pos1) != Blocks.AIR && BlockUtil.getBlock(pos1.add(0, 1, 0)) == Blocks.AIR) {
                    if (jumpTimer.hasPassedMs(jumpTimerDelay.getValue())) {
                        mc.player.jump();
                        canBoost = true;
                        jumpTimer.reset();
                    }
                }
            }
        }

        if(canBoost) {
            if (mc.player.motionY > 0) {
                ((IKeyBinding) mc.gameSettings.keyBindSneak).setPressed(true);
            } else if (mc.player.motionY < 0) {
                if (fowardBoostTimer.hasPassedMs(boostTimer.getValue())) {
                    ((IKeyBinding) mc.gameSettings.keyBindSneak).setPressed(false);

                    double[] calc = MathUtil.directionSpeed(this.vanillaSpeed.getValue() / 10.0);
                    mc.player.motionX = calc[0];
                    mc.player.motionZ = calc[1];

                    canBoost = false;
                    fowardBoostTimer.reset();
                }
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
