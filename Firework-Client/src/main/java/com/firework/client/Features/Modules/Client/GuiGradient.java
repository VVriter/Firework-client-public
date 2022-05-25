package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

public class GuiGradient extends Module {

    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> rainbow1  = null;
    public static Setting<Double> red1  = null;
    public static Setting<Double> green1   = null;
    public static Setting<Double> blue1   = null;
    public static Setting<Double> alpha1    = null;

    public static Setting<Boolean> rainbow2   = null;
    public static Setting<Double> red2  = null;
    public static Setting<Double> green2   = null;
    public static Setting<Double> blue2  = null;
    public static Setting<Double> alpha2    = null;

    public GuiGradient(){super("GuiGradient",Category.CLIENT);
        enabled = this.isEnabled;

        rainbow1 = new Setting<>("rainbow1", false, this);
        red1 =  new Setting<>("red1", (double)0, this, 0, 255);
        green1 =  new Setting<>("green1", (double)0, this, 0, 255);
        blue1 =  new Setting<>("blue1", (double)0, this, 0, 255);
        alpha1 =  new Setting<>("alpha1", (double)0, this, 0, 255);

        rainbow2 = new Setting<>("rainbow1", false, this);
        red2 =  new Setting<>("red1", (double)0, this, 0, 255);
        green2 =  new Setting<>("green1", (double)0, this, 0, 255);
        blue2 =  new Setting<>("blue1", (double)0, this, 0, 255);
        alpha2 =  new Setting<>("alpha1", (double)0, this, 0, 255);


    }
}
