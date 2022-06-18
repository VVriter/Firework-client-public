package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class ItemViewModel extends Module {

    public static Setting<Boolean> enabled = null;

    public Setting<String> mode = new Setting<>("Mode", "Mixin", this, Arrays.asList("GL", "Mixin"));

        public static Setting<Double> translateX  = null;
        public static Setting<Double> translateY   = null;
        public static Setting<Double> translateZ   = null;

        public static Setting<Double> rotateXR   = null;
        public static Setting<Double> rotateYR   = null;
        public static Setting<Double> rotateZR   = null;

        public static Setting<Double> rotateXL  = null;
        public static Setting<Double> rotateYL   = null;
        public static Setting<Double> rotateZL   = null;

        public static Setting<Double> scaleX   = null;
        public static Setting<Double> scaleY   = null;
        public static Setting<Double> scaleZ   = null;

    public ItemViewModel(){super("ItemViewModel",Category.RENDER);

        enabled = this.isEnabled;
        translateX = new Setting<>("translateX", (double)0,this, -300, 300);
        translateY = new Setting<>("translateY", (double)0,this, -300, 300);
        translateZ = new Setting<>("translateZ", (double)0,this, -300, 300);

        rotateXR = new Setting<>("rotateXR", (double)0,this, -300, 300);
        rotateYR = new Setting<>("rotateYR", (double)0,this, -300, 300);
        rotateZR = new Setting<>("rotateZR", (double)0,this, -300, 300);

        rotateXL = new Setting<>("rotateXL", (double)0,this, -300, 300);
        rotateYL = new Setting<>("rotateYL", (double)0,this, -300, 300);
        rotateZL = new Setting<>("rotateZL", (double)0,this, -300, 300);

        scaleX = new Setting<>("scaleX", (double)100,this, -300, 300);
        scaleY = new Setting<>("scaleY", (double)100,this, -300, 300);
        scaleZ = new Setting<>("scaleZ", (double)100,this, -300, 300);
        }
    }




