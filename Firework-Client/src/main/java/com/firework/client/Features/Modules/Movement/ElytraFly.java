package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import net.minecraft.network.play.client.CPacketPlayer;

@ModuleManifest(name = "ElytraFly",category = Module.Category.MOVEMENT)
public class ElytraFly extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Control, this, modes.values());
    public enum modes{
        Control
    }
    public Setting<Double> VerticalSpeed = new Setting<>("VerticalSpeed", (double)3, this, 1, 20).setVisibility(mode,modes.Control);
    public Setting<Double> UpSpeed = new Setting<>("UpSpeed", (double)0.5, this, 1, 2).setVisibility(mode,modes.Control);
    public Setting<Double> DownSpeed = new Setting<>("DownSpeed", (double)0.5, this, 1, 2).setVisibility(mode,modes.Control);

    public Setting<Boolean> yawControl = new Setting<>("YawControl", true, this);

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
                     mc.player.rotationPitch = -30;
                 }
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY = -DownSpeed.getValue();
                }
                double[] calc = MathUtil.directionSpeed(this.VerticalSpeed.getValue() / 10.0);
                mc.player.motionX = calc[0];
                mc.player.motionZ = calc[1];





            }
        }
    }
}
