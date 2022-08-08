package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(
        name = "ItemViewModifier",
        category = Module.Category.VISUALS,
        description = ""
)
public class ItemModel extends Module {
    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> noEat = null;
    public  Setting<Boolean> translateSubBool = null;
    public static Setting<Double> TRX = null;
    public static Setting<Double> TRY = null;
    public static Setting<Double> TRZ = null;
    public static Setting<Double> TLX = null;
    public static Setting<Double> TLY = null;
    public static Setting<Double> TLZ = null;

    public  Setting<Boolean> rotateSubBool = null;
    public static Setting<Double> RRX = null;
    public static Setting<Double> RRY = null;
    public static Setting<Double> RRZ = null;
    public static Setting<Double> RLX = null;
    public static Setting<Double> RLY = null;
    public static Setting<Double> RLZ = null;

    public  Setting<Boolean> scaleSubBool = null;
    public static Setting<Double> RSX = null;
    public static Setting<Double> RSY = null;
    public static Setting<Double> RSZ = null;
    public static Setting<Double> LSX = null;
    public static Setting<Double> LSY = null;
    public static Setting<Double> LSZ = null;

    public static Setting<Integer> swingSpeed = null;

    public ItemModel() {
        enabled = this.isEnabled;

        noEat = new Setting<>("NoEat", true, this);

        translateSubBool = new Setting<>("Translate", false, this).setMode(Setting.Mode.SUB);
        TRX = new Setting<>("RX", (double)0, this, -300, 300).setVisibility(v-> translateSubBool.getValue());
        TRY = new Setting<>("RY", (double)0, this, -300, 300).setVisibility(v-> translateSubBool.getValue());
        TRZ = new Setting<>("RZ", (double)0, this, -300, 300).setVisibility(v-> translateSubBool.getValue());
        TLX = new Setting<>("LX", (double)0, this, -300, 300).setVisibility(v-> translateSubBool.getValue());
        TLY = new Setting<>("LY", (double)0, this, -300, 300).setVisibility(v-> translateSubBool.getValue());
        TLZ = new Setting<>("LZ", (double)0, this, -300, 300).setVisibility(v-> translateSubBool.getValue());

        rotateSubBool = new Setting<>("Rotate", false, this).setMode(Setting.Mode.SUB);
        RRX = new Setting<>("RRX", (double)0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RRY = new Setting<>("RRY", (double)0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RRZ = new Setting<>("RRZ", (double)0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RLX = new Setting<>("RLX", (double)0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RLY = new Setting<>("RLY", (double)0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RLZ = new Setting<>("RLZ", (double)0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());

        scaleSubBool = new Setting<>("Scale", false, this).setMode(Setting.Mode.SUB);
        RSX = new Setting<>("RSX", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        RSY = new Setting<>("RSY", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        RSZ = new Setting<>("RSZ", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        LSX = new Setting<>("LSX", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        LSY = new Setting<>("LSY", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        LSZ = new Setting<>("LSZ", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());

        swingSpeed = new Setting<>("SwingSpeed", 20, this, 1, 40);
    }
}
