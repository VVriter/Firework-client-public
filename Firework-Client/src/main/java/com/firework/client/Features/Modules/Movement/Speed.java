package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Timer;


@ModuleArgs(name = "Speed",category = Module.Category.MOVEMENT)
public class Speed extends Module{


    private double playerSpeed;
    private final Timer timer = new Timer();



    public Setting<Enum> mode = new Setting<>("Mode", modes.Vanilla, this, modes.values());
    public Setting<Double> vanillaSpeed = new Setting<>("Speed", (double)3, this, 1, 20).setVisibility(mode,modes.Vanilla);
    public Setting<Boolean> step = new Setting<>("Step", true, this).setVisibility(mode,modes.YPort);
    public Setting<Double> yPortSpeed = new Setting<>("Speed", (double)3, this, 1, 20).setVisibility(mode,modes.YPort);



    @Override
    public void onEnable(){
        super.onEnable();
        playerSpeed = PlayerUtil.getBaseMoveSpeed();
    }


    @Override
    public void onDisable() {
        super.onDisable();
        this.timer.reset();
        if (step.getValue()) {
            mc.player.stepHeight = 0.6f;
        }
    }



    @Override
    public void onTick(){
        super.onTick();


        if (step.getValue()) {
            mc.player.stepHeight = 2.0f;
        }if (step.getValue() == false) {
            mc.player.stepHeight = 0.6f;
        }





        if (mode.getValue() == modes.Vanilla) {
            if (mc.player == null || mc.world == null) {
                return;
            }
            double[] calc = MathUtil.directionSpeed(this.vanillaSpeed.getValue() / 10.0);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
        }

        if (mode.getValue() == modes.YPort) {
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
        }
    }

    public enum modes{
        Vanilla, YPort
    }
}