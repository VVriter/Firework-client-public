package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import org.lwjgl.input.Keyboard;

public class ItemPhysics extends Module {

    public static Setting<Double> Scaling = null;
    public static Setting<Boolean> enabled = null;

    public ItemPhysics(){super("ItemPhysics",Category.RENDER);
        enabled = this.isEnabled;
        Scaling = new Setting<>("Scale", (double)3, this, 0.5, 10);
    }
}
