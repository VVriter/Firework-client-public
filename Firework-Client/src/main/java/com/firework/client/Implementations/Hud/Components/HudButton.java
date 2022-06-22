package com.firework.client.Implementations.Hud.Components;

import com.firework.client.Implementations.Hud.Huds.HudComponent;
import net.minecraft.util.math.Vec2f;

public class HudButton extends Button{

    public HudComponent hudComponent;

    public HudButton(HudComponent hudComponent, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hudComponent = hudComponent;
    }

    @Override
    public void initialize(Vec2f point, int state) {
        super.initialize(point, state);
        if(state == 0)
            hudComponent.setEnabled(!hudComponent.enabled);
    }
}
