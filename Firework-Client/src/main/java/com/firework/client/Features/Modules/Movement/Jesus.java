package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.IKeyBinding;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
@ModuleManifest(name = "Jesus",category = Module.Category.MOVEMENT)
public class Jesus extends Module {
    public Setting<Enum> mode = new Setting<>("Mode", modes.Bypass, this, modes.values());
    public enum modes{
        Bypass, Strict, Bounce, Fly
    }
    public Setting<Boolean> speedBool = new Setting<>("Speed", true, this);
    public Setting<Double> speedDouble = new Setting<>("Horizontal", (double)2.1, this, 1, 5).setVisibility(speedBool,true);

    public Setting<Double> UpSped = new Setting<>("UpSpeed", (double)0.3, this, 0.1, 1).setVisibility(mode,modes.Fly);
    public Setting<Double> DpwnSped = new Setting<>("DownSpeed", (double)0.3, this, 0.1, 1).setVisibility(mode,modes.Fly);
    public Setting<Boolean> antikick = new Setting<>("AntiKick", true, this).setVisibility(mode, modes.Fly);
    @Override
    public void onTick () { super.onTick();
        if (mc.player.isInWater() || mc.player.isInLava()) {
            if(mode.getValue(modes.Bypass)) {
                mc.player.motionY = 0;
            } else if (mode.getValue(modes.Bounce)) {
                mc.player.motionY = 0;
                mc.player.jump();
            } else if (mode.getValue(modes.Strict)) {
                mc.player.motionY = 0;
                ((IKeyBinding)mc.gameSettings.keyBindJump).setPressed(true);
            } else if (mode.getValue(modes.Fly)) {
                if (antikick.getValue()) {
                    mc.player.motionY = 0;
                } if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY = UpSped.getValue()/2;
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY = 0-DpwnSped.getValue()/2;
                }



            } else {
                ((IKeyBinding)mc.gameSettings.keyBindJump).setPressed(false);
            }
        }

        if (mc.player.onGround) {
            ((IKeyBinding)mc.gameSettings.keyBindJump).setPressed(false);
        }

        if (speedBool.getValue() && !mc.player.collidedHorizontally && mc.player.isInWater() || mc.player.isInLava()) {
            if (mc.player == null || mc.world == null) {return;}
            double[] calc = MathUtil.directionSpeed(this.speedDouble.getValue() / 10.0);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
        }

        if (mc.player.collidedHorizontally && Jesus.mc.gameSettings.keyBindJump.isKeyDown() && mc.player.isInWater() || mc.player.isInLava()) {
            mc.player.motionY = 0.2;
        }

        if (!mode.getValue(modes.Fly) && mc.gameSettings.keyBindSneak.isKeyDown() &&  mc.player.isInWater() || mc.player.isInLava()) {
            mc.player.motionY = -0.1;
        }
    }
}
