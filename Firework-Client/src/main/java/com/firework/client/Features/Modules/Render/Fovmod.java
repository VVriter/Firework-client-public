package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;

@ModuleArgs(name = "FovMod",category = Module.Category.RENDER)
public class Fovmod extends Module {

    public float defaultFov;

    public Setting<Double> Change  = new Setting<>("Change ", (double)100, this, 0, 500);
    public Setting<Boolean> Smooth  = new Setting<>("Smooth ", false, this);
    public Setting<String> FovMode  = new Setting<>("FovMode", "ViewModelChanger", this, Arrays.asList("ViewModelChanger", "FovChanger","Zoom"));

    @SubscribeEvent
    public void FOVModifier(EntityViewRenderEvent.FOVModifier event) {
        if (FovMode.getValue().equals("ViewModelChanger")) {
            event.setFOV((float) Change.getValue().floatValue());
        }
    }
    @Override
    public void onEnable() {
        super.onEnable();
        defaultFov = mc.gameSettings.fovSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.fovSetting = defaultFov;
        mc.gameSettings.smoothCamera = false;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        mc.gameSettings.smoothCamera = Smooth.getValue();
        if (FovMode.getValue().equals("FovChanger")) {
            mc.gameSettings.fovSetting = (float) Change.getValue().floatValue();
        }
        if (FovMode.getValue().equals("Zoom")) {
            if (mc.gameSettings.fovSetting > 12f) {
                for (int i = 0; i < Change.getValue(); i++) {
                    if (mc.gameSettings.fovSetting > 12f) {
                        mc.gameSettings.fovSetting -= 0.1f;
                    }
                }
            } else if (mc.gameSettings.fovSetting < this.defaultFov) {
                for (int i = 0; i < Change.getValue(); i++) {
                    mc.gameSettings.fovSetting += 0.1F;
                }
            }
        }
    }


}
