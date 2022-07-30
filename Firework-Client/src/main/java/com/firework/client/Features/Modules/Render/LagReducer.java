package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import org.lwjgl.opengl.Display;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "LagReducer",
        category = Module.Category.VISUALS,
        description = "Reduce ur lags"
)
public class LagReducer extends Module {

    public Setting<Integer> limit = new Setting<>("Limit", 10, this, 1, 50);

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> eventListener = new Listener<>(event-> {
        if (Display.isActive()) {
            mc.gameSettings.limitFramerate = 255;
        } else {
            mc.gameSettings.limitFramerate = limit.getValue().intValue();
        }
    });
}
