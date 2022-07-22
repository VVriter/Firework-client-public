package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

public class ItemPhysics extends Module {

    public static Setting<Double> scaling = null;
    public static Setting<Boolean> enabled = null;

    public ItemPhysics(){super("ItemPhysics",Category.VISUALS);
        enabled = this.isEnabled;
        scaling = new Setting<>("Scale", 1d, this, 0.1, 10);
    }
}
