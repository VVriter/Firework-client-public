package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

public class ItemPhysics extends Module {

    public static ItemPhysics INSTANCE = new ItemPhysics();
    public Setting<Double> Scaling = new Setting<>("Scale", (double)3, this, 0.5, 10);

    public ItemPhysics(){super("ItemPhysics",Category.RENDER);
        this.setInstance();}




    public static ItemPhysics getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemPhysics();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}
