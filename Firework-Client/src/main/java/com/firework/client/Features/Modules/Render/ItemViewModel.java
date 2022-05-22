package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

public class ItemViewModel extends Module {

    public static Setting<Boolean> enabled = null;

        public static Setting<Double> translateX  = null;
        public static Setting<Double> translateY   = null;
        public static Setting<Double> translateZ   = null;

        public static Setting<Double> rotateX   = null;
        public static Setting<Double> rotateY   = null;
        public static Setting<Double> rotateZ   = null;

        public static Setting<Double> scaleX   = null;
        public static Setting<Double> scaleY   = null;
        public static Setting<Double> scaleZ   = null;

    public ItemViewModel(){super("ItemViewModel",Category.RENDER);

        enabled = this.isEnabled;

        translateX = new Setting<>("translateX", (double)0,this, -300, 300);
        translateY = new Setting<>("translateY", (double)0,this, -300, 300);
        translateZ = new Setting<>("translateZ", (double)0,this, -300, 300);

        rotateX = new Setting<>("rotateX", (double)0,this, -300, 300);
        rotateY = new Setting<>("rotateY", (double)0,this, -300, 300);
        rotateZ = new Setting<>("rotateZ", (double)0,this, -300, 300);

        scaleX = new Setting<>("scaleX", (double)100,this, -300, 300);
        scaleY = new Setting<>("scaleY", (double)100,this, -300, 300);
        scaleZ = new Setting<>("scaleZ", (double)100,this, -300, 300);}





}
