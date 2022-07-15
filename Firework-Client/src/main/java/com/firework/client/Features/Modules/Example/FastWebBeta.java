package com.firework.client.Features.Modules.Example;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.IEntity;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(
        name = "FastWebBeta",
        category = Module.Category.EXAMPLE
)
public class FastWebBeta extends Module {
    public Setting<Enum> mode = new Setting<>("Mode", modes.Motion, this, modes.values());
    public enum modes {
        Timer, Motion
    }
    public Setting<Double> reduction = new Setting<>("Reduction", (double)0.3, this, 0, 2).setVisibility(mode,modes.Motion);
    public Setting<Double> ticks = new Setting<>("Reduction", (double)45, this, 40, 60).setVisibility(mode,modes.Timer);;


    @Override public void onEnable() { super.onEnable();
        oldTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
    }

    @Override public void onDisable() { super.onDisable();
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (((IEntity)mc.player).isInWeb()) {
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                pushDown();
            }
        }

        if (((IEntity)mc.player).isInWeb() && mode.getValue(modes.Timer)) {
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks-ticks.getValue().floatValue());
            }
        }

        if (!(mc.gameSettings.keyBindSneak.isKeyDown())) {
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks);
        }
    }


    float oldTicks;
    void pushDown() {
        if (mode.getValue(modes.Motion)) {
            mc.player.motionY = -reduction.getValue();
        }
    }
}
