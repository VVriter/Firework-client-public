package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Globals;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


@ModuleManifest(name = "Speed",category = Module.Category.MOVEMENT)
public class Speed extends Module{

    private double playerSpeed;
    private final Timer timer = new Timer();

    public Setting<Enum> mode = new Setting<>("Mode", modes.BHop, this);
    public Setting<Double> vanillaSpeed = new Setting<>("VanillaSpeed", (double)3, this, 1, 20).setVisibility(v-> mode.getValue(modes.Vanilla));
    public Setting<Boolean> step = new Setting<>("Step", true, this).setVisibility(v-> mode.getValue(modes.YPort));
    public Setting<Double> yPortSpeed = new Setting<>("Speed", (double)3, this, 1, 20).setVisibility(v-> mode.getValue(modes.YPort));

    public Setting<Double> strafeMultipler = new Setting<>("StrafeMultipler", 5.2d, this, 0, 20).setVisibility(v-> mode.getValue(modes.Strafe));
    public Setting<Boolean> boost = new Setting<>("Boost", true, this).setVisibility(v-> mode.getValue(modes.Strafe));
    public Setting<Double> ticks = new Setting<>("Ticks", 4.3d, this, 0, 50).setVisibility(v-> boost.getValue() && mode.getValue(modes.Strafe));

    float defaultTimerTicks;
    @Override
    public void onEnable(){
        super.onEnable();
        playerSpeed = PlayerUtil.getBaseMoveSpeed();
        defaultTimerTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
        if(fullNullCheck()) onDisable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.timer.reset();
        if (step.getValue()) {
            mc.player.stepHeight = 0.6f;
        }
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks);
    }
    @Override
    public void onTick(){
        super.onTick();
        if(fullNullCheck()) return;

        if(mode.getValue(modes.Strafe)){
            if(boost.getValue())
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTimerTicks - ticks.getValue().floatValue());

            mc.player.stepHeight = 0.6f;
            if (mc.player.onGround) {
                if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                    mc.player.jump();
                    mc.player.setSprinting(true);
                    double[] dir = MathUtil.directionSpeed(strafeMultipler.getValue()/20);
                    mc.player.motionX = dir[0];
                    mc.player.motionZ = dir[1];
                }
            } else {
                double[] dir = MathUtil.directionSpeed(strafeMultipler.getValue()/20);
                mc.player.motionX = dir[0];
                mc.player.motionZ = dir[1];
            }
        }else if (mode.getValue() == modes.Vanilla) {
            if (mc.player == null || mc.world == null) {
                return;
            }
            double[] calc = MathUtil.directionSpeed(this.vanillaSpeed.getValue() / 10.0);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
        }else if (mode.getValue(modes.YPort)) {
            if (step.getValue()) {
                mc.player.stepHeight = 2.0f;
            }if (step.getValue() == false) {
                mc.player.stepHeight = 0.6f;
            }
            if (!PlayerUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
                return;
            }
            if (mc.player.onGround) {
                //EntityUtil.setTimer(1.15f);
                mc.player.jump();
                PlayerUtil.setSpeed(mc.player, PlayerUtil.getBaseMoveSpeed() + yPortSpeed.getValue() / 10);
            } else {
                mc.player.motionY = -1;
                //EntityUtil.resetTimer();
            }
        }else if(mode.getValue(modes.BHop)){
            if (mc.player.onGround) {
                mc.player.stepHeight = 0.6f;
                mc.player.jump();
            }
        }else if(mode.getValue(modes.MiniJumps)){
            if (mc.player.onGround) {
                mc.player.jump();
                mc.player.motionY = 0.25;
            }
        } else if (mode.getValue(modes.TunnelSpeed)) {
            BlockPos pos = new BlockPos(Globals.mc.player.posX, Globals.mc.player.posY + 2.0, Globals.mc.player.posZ);
            BlockPos pos2 = new BlockPos(Globals.mc.player.posX, Globals.mc.player.posY - 1.0, Globals.mc.player.posZ);
            if (Globals.mc.world.getBlockState(pos).getBlock() != Blocks.AIR && Globals.mc.world.getBlockState(pos).getBlock() != Blocks.PORTAL && Globals.mc.world.getBlockState(pos).getBlock() != Blocks.END_PORTAL && Globals.mc.world.getBlockState(pos).getBlock() != Blocks.WATER && Globals.mc.world.getBlockState(pos).getBlock() != Blocks.FLOWING_WATER && Globals.mc.world.getBlockState(pos).getBlock() != Blocks.LAVA && Globals.mc.world.getBlockState(pos).getBlock() != Blocks.FLOWING_LAVA && Globals.mc.world.getBlockState(pos2).getBlock() != Blocks.ICE && Globals.mc.world.getBlockState(pos2).getBlock() != Blocks.FROSTED_ICE && Globals.mc.world.getBlockState(pos2).getBlock() != Blocks.PACKED_ICE && !Globals.mc.player.isInWater()) {
                float yaw = (float)Math.toRadians(mc.player.rotationYaw);
                if (Globals.mc.gameSettings.keyBindForward.isKeyDown() && !Globals.mc.gameSettings.keyBindSneak.isKeyDown() && Globals.mc.player.onGround) {
                    Globals.mc.player.motionX -= (double)Math.sin(yaw) * 0.15;
                    Globals.mc.player.motionZ += (double)Math.cos(yaw) * 0.15;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent e) {

    }
    public enum modes{
        Vanilla, YPort, Strafe, BHop, MiniJumps, TunnelSpeed
    }
}