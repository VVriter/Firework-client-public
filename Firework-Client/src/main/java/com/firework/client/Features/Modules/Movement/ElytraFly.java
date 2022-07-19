package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;

@ModuleManifest(name = "ElytraFly",category = Module.Category.MOVEMENT)
public class ElytraFly extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Control, this, modes.values());
    public enum modes{
        Control, Firework
    }

    //Controll Shit
    public Setting<Double> VerticalSpeed = new Setting<>("VerticalSpeed", (double)3, this, 1, 20).setVisibility(v-> mode.getValue(modes.Control));
    public Setting<Double> UpSpeed = new Setting<>("UpSpeed", (double)0.5, this, 1, 2).setVisibility(v-> mode.getValue(modes.Control));
    public Setting<Double> DownSpeed = new Setting<>("DownSpeed", (double)0.5, this, 1, 2).setVisibility(v-> mode.getValue(modes.Control));

    public Setting<Boolean> yawControl = new Setting<>("YawControl", true, this).setVisibility(v-> mode.getValue(modes.Control));

    public Setting<Boolean> autostart = new Setting<>("Autostart", false, this).setMode(Setting.Mode.SUB).setVisibility(v-> mode.getValue(modes.Firework));

    public Setting<Boolean> enableAutostart = new Setting<>("EnableAutostart", true, this).setVisibility(v-> autostart.getValue() && mode.getValue(modes.Firework));
    public Setting<Boolean> jump = new Setting<>("Jump", true, this).setVisibility(v-> autostart.getValue() && mode.getValue(modes.Firework));
    public Setting<Boolean> useTimer = new Setting<>("UseTimer", true, this).setVisibility(v-> autostart.getValue() && mode.getValue(modes.Firework));



    float defaultTickLeght;
    @Override
    public void onTick() {
        super.onTick();
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
        }
    }


    @Override
    public void onEnable() {
        super.onEnable();
        defaultTickLeght =  ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(defaultTickLeght);
    }
}
