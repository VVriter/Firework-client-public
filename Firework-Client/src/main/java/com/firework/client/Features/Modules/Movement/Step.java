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
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "Step", category = Module.Category.MOVEMENT)
public class Step extends Module {
    public Setting<modes> moderio = new Setting<>("Mode", modes.MegaBypass, this, modes.values());
    public enum modes{
       MegaBypass, Vanilla
    }
    public Setting<Boolean> stopMotion = new Setting<>("StopMotion", true, this).setVisibility(moderio,modes.MegaBypass);
    public Setting<Boolean> shouldSneak = new Setting<>("Sneak", false, this).setVisibility(moderio,modes.MegaBypass);
    public Setting<Boolean> forceGroundPacket = new Setting<>("ForceGround", true, this).setVisibility(moderio,modes.MegaBypass);
    public Setting<Double> vanillaSpeed = new Setting<>("Speed", 2.1d, this, 1, 20).setVisibility(moderio,modes.MegaBypass);

    public Setting<Double> boostTimer = new Setting<>("FWBoostDelayMs", 29.8d, this, 1, 200).setVisibility(moderio,modes.MegaBypass);
    public Setting<Double> jumpTimerDelay = new Setting<>("JumpDelayMs", 185.6d, this, 1, 200).setVisibility(moderio,modes.MegaBypass);

    public Setting<mode> moveMode = new Setting<>("Mode", mode.Motion, this, mode.values()).setVisibility(moderio,modes.MegaBypass);

    public Setting<Integer> Y = new Setting<>("Height", 1, this, 0, 10).setVisibility(moderio,modes.Vanilla);
    public Setting<Boolean> reverse = new Setting<>("Reverse", false, this).setVisibility(moderio,modes.Vanilla);
    public Setting<Boolean> entityStep = new Setting<>("EntityStep", false, this).setVisibility(moderio,modes.Vanilla);
    public enum mode{
        Motion, Tp, Combined
    }
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

        if (moderio.getValue(modes.Vanilla)) {
            mc.player.stepHeight = Y.getValue().floatValue();
            if(reverse.getValue()){
                if(mc.player.onGround){
                    mc.player.motionY -= 1.0;
                }
            }

            if(entityStep.getValue() && mc.player.isRiding()){
                mc.player.getRidingEntity().stepHeight = 1;
            }
        }

        if (moderio.getValue(modes.MegaBypass)) {
            if (mc.player.collidedHorizontally && checkFallDistance(1)) {
                BlockPos playerPos = EntityUtil.getFlooredPos(mc.player);
                BlockPos pos1 = playerPos.offset(mc.player.getHorizontalFacing());
                if (BlockUtil.getBlock(pos1) != Blocks.AIR && BlockUtil.getBlock(pos1.add(0, 1, 0)) == Blocks.AIR
                        && BlockUtil.getBlock(pos1.add(0, 2, 0)) == Blocks.AIR
                        && BlockUtil.getBlock(playerPos.add(0, 2, 0)) == Blocks.AIR) {
                    if (jumpTimer.hasPassedMs(jumpTimerDelay.getValue())) {
                        if (stopMotion.getValue()) {
                            mc.player.motionX = 0;
                            mc.player.motionZ = 0;
                        }
                        mc.player.jump();
                        canBoost = true;
                        jumpTimer.reset();
                    }
                }
            }

            if (canBoost) {
                if (mc.player.motionY > 0) {
                    if (shouldSneak.getValue())
                        ((IKeyBinding) mc.gameSettings.keyBindSneak).setPressed(true);
                } else if (mc.player.motionY < 0) {
                    if (fowardBoostTimer.hasPassedMs(boostTimer.getValue())) {
                        if (shouldSneak.getValue())
                            ((IKeyBinding) mc.gameSettings.keyBindSneak).setPressed(false);

                        if (moveMode.getValue(mode.Motion)) {
                            double[] calc = MathUtil.directionSpeed(this.vanillaSpeed.getValue() / 10.0);
                            mc.player.motionX = calc[0];
                            mc.player.motionZ = calc[1];
                        } else if (moveMode.getValue(mode.Tp)) {
                            BlockPos playerPos = EntityUtil.getFlooredPos(mc.player);
                            BlockPos pos1 = playerPos.offset(mc.player.getHorizontalFacing());

                            EnumFacing facing = mc.player.getHorizontalFacing();
                            Vec3d vec3d = offset(mc.player.getPositionVector(), facing);
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(vec3d.x, mc.player.posY, vec3d.z, mc.player.onGround));
                            mc.player.setPosition(vec3d.x, mc.player.posY, vec3d.z);
                        } else if (moveMode.getValue(mode.Combined)) {
                            double[] calc = MathUtil.directionSpeed(this.vanillaSpeed.getValue() / 10.0);
                            mc.player.motionX = calc[0];
                            mc.player.motionZ = calc[1];
                            BlockPos playerPos = EntityUtil.getFlooredPos(mc.player);
                            BlockPos pos1 = playerPos.offset(mc.player.getHorizontalFacing());
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(pos1.getX() + 0.5, mc.player.posY, pos1.getZ() + 0.5, mc.player.onGround));
                            mc.player.setPosition(pos1.getX() + 0.5, mc.player.posY, pos1.getZ() + 0.5);
                        }

                        canBoost = false;
                        fowardBoostTimer.reset();
                    }
                }
            }
        }
    }

    public Vec3d offset(Vec3d vec3d, EnumFacing facing){
        return new Vec3d(vec3d.x + facing.getXOffset(), vec3d.y + facing.getYOffset(), vec3d.z + facing.getZOffset());
    }

    public boolean checkFallDistance(int distance){
        BlockPos playerPos = EntityUtil.getFlooredPos(mc.player);
        for(int i = 0; i < distance; i++){
            if(BlockUtil.getBlock(playerPos.add(0, -(i + 1), 0)) != Blocks.AIR)
                return true;
            else
                return false;
        }
        return false;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (moderio.getValue(modes.MegaBypass)) {
        if (event.getPacket() instanceof CPacketPlayer && forceGroundPacket.getValue()) {
            ((ICPacketPlayer) event.getPacket()).setOnGround(true);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
            mc.gameSettings.autoJump = oldAutoJump;
            mc.player.stepHeight = 0.6f;
    }
}
