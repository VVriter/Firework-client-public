package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "ElytraFly",
        category = Module.Category.MOVEMENT,
        description = "Fly using elytra"
)
public class ElytraFly extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Control, this);
    public enum modes{
        Control, Firework
    }

    //Controll Shit
    public Setting<Double> VerticalSpeed = new Setting<>("VerticalSpeed", (double)3, this, 1, 20).setVisibility(()-> mode.getValue(modes.Control));
    public Setting<Double> UpSpeed = new Setting<>("UpSpeed", (double)0.5, this, 1, 2).setVisibility(()-> mode.getValue(modes.Control));
    public Setting<Double> DownSpeed = new Setting<>("DownSpeed", (double)0.5, this, 1, 2).setVisibility(()-> mode.getValue(modes.Control));

    public Setting<Boolean> yawControl = new Setting<>("YawControl", true, this).setVisibility(()-> mode.getValue(modes.Control));

    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this).setVisibility(()-> mode.getValue(modes.Firework));
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(()-> mode.getValue(modes.Firework));
    public Setting<Double> delay = new Setting<>("UseDelay", (double)500, this, 1, 3000).setVisibility(()-> mode.getValue(modes.Firework));
    public Setting<Boolean> autostart = new Setting<>("Autostart", false, this).setMode(Setting.Mode.SUB).setVisibility(()-> mode.getValue(modes.Firework));
    public Setting<Boolean> enableAutostart = new Setting<>("EnableAutostart", true, this).setVisibility(()-> autostart.getValue() && mode.getValue(modes.Firework));
    public Setting<Boolean> jump = new Setting<>("Jump", true, this).setVisibility(()-> autostart.getValue() && mode.getValue(modes.Firework));
    public Setting<Boolean> useTimer = new Setting<>("UseTimer", true, this).setVisibility(()-> autostart.getValue() && mode.getValue(modes.Firework));

    public Setting<Boolean> toobeetoteeBypass = new Setting<>("2b2t", true, this).setVisibility(()-> mode.getValue(modes.Firework));
    public Setting<Boolean> timerReset = new Setting<>("TimerReset", true, this).setVisibility(()-> mode.getValue(modes.Firework) && toobeetoteeBypass.getValue());
    public Setting<Double> delayAutostart = new Setting<>("TimerDelay", (double)2000, this, 1, 5000).setVisibility(()-> mode.getValue(modes.Firework) && toobeetoteeBypass.getValue());

    float defaultTickLeght;
    ItemUser user;
    Timer timer = new Timer();
    Timer startTimer = new Timer();
    boolean needToFireFly;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (mc.player.isElytraFlying()) {
            //Control
            if (mode.getValue(modes.Control)) {
                mc.player.setVelocity(0.0D, 0.0D, 0.0D);

                //Controls
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                 mc.player.motionY = UpSpeed.getValue();
                 if (yawControl.getValue()) {
                     mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -30, false));
                 }
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY = -DownSpeed.getValue();
                }
                double[] calc = MathUtil.directionSpeed(this.VerticalSpeed.getValue() / 10.0);
                mc.player.motionX = calc[0];
                mc.player.motionZ = calc[1];
            }
        }

        if (mode.getValue(modes.Firework)) {
            if (mc.player.isElytraFlying() && needToFireFly) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght);
                if (timer.hasPassedMs(delay.getValue())) {
                    user.useItem(Items.FIREWORKS,0);
                    timer.reset();
                    }
                }
            }

        if (mc.player.isElytraFlying() && !toobeetoteeBypass.getValue()) {
            needToFireFly = true;
        }

        if (mc.player.isElytraFlying() && startTimer.hasPassedMs(delayAutostart.getValue())) {
            needToFireFly = true;
            if (timerReset.getValue()) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght);
            }
        }

        if (!mc.player.isElytraFlying()) {
            needToFireFly = false;
        }

        if (mc.player.onGround) {
            startTimer.reset();
        }

            //Autostart
            if (enableAutostart.getValue()) {
            if (!mc.player.isElytraFlying()) {
                if (jump.getValue() && mc.player.onGround) {
                    mc.player.jump();
                } if (useTimer.getValue()) {
                    if (!mc.player.onGround) {
                        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(150);
                    } else if (mc.player.onGround){
                            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght);
                        }
                    }
                }
            }
    });

    @Override
    public void onEnable() {
        super.onEnable();
        startTimer.reset();
        user = new ItemUser(this,switchMode,rotate);
        defaultTickLeght =  ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        startTimer.reset();
        user = null;
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght);
    }
}
