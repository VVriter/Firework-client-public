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
        Bypass, Strict, Bounce
    }

    public Setting<Boolean> speedBool = new Setting<>("Speed", true, this);
    public Setting<Double> speedDouble = new Setting<>("Reduction", (double)2.1, this, 1, 5).setVisibility(speedBool,true);
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
            } else {
                ((IKeyBinding)mc.gameSettings.keyBindJump).setPressed(false);
            }
        }
        if (speedBool.getValue() && mc.player.isInWater() || mc.player.isInLava()) {
            if (mc.player == null || mc.world == null) {return;}
            double[] calc = MathUtil.directionSpeed(this.speedDouble.getValue() / 10.0);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
        }
    }
}
