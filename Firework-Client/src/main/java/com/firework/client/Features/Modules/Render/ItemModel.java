package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

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
    public static Setting<Integer> RRX = null;
    public static Setting<Integer> RRY = null;
    public static Setting<Integer> RRZ = null;
    public static Setting<Integer> RLX = null;
    public static Setting<Integer> RLY = null;
    public static Setting<Integer> RLZ = null;

    public  Setting<Boolean> scaleSubBool = null;
    public static Setting<Double> RSX = null;
    public static Setting<Double> RSY = null;
    public static Setting<Double> RSZ = null;
    public static Setting<Double> LSX = null;
    public static Setting<Double> LSY = null;
    public static Setting<Double> LSZ = null;
    public  Setting<Boolean> animationSubBool = null;
    public static Setting<Integer> addAmount = null;
    public static Setting<Boolean> xr = null;
    public static Setting<Boolean> yr = null;
    public static Setting<Boolean> zr = null;
    public static Setting<Boolean> xl = null;
    public static Setting<Boolean> yl = null;
    public static Setting<Boolean> zl = null;


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
        RRX = new Setting<>("RRX", 0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RRY = new Setting<>("RRY", 0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RRZ = new Setting<>("RRZ", 0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RLX = new Setting<>("RLX", 0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RLY = new Setting<>("RLY", 0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());
        RLZ = new Setting<>("RLZ", 0, this, -300, 300).setVisibility(v-> rotateSubBool.getValue());

        scaleSubBool = new Setting<>("Scale", false, this).setMode(Setting.Mode.SUB);
        RSX = new Setting<>("RSX", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        RSY = new Setting<>("RSY", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        RSZ = new Setting<>("RSZ", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        LSX = new Setting<>("LSX", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        LSY = new Setting<>("LSY", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());
        LSZ = new Setting<>("LSZ", (double)100, this, -300, 300).setVisibility(v-> scaleSubBool.getValue());

        animationSubBool = new Setting<>("Animations", false, this).setMode(Setting.Mode.SUB);
        addAmount = new Setting<>("AddAmount", 3, this, 0, 10).setVisibility(v-> animationSubBool.getValue());
        xr = new Setting<>("XRA", true, this).setVisibility(v-> animationSubBool.getValue());
        yr = new Setting<>("YRA", true, this).setVisibility(v-> animationSubBool.getValue());
        zr = new Setting<>("ZRA", true, this).setVisibility(v-> animationSubBool.getValue());
        xl = new Setting<>("XLA", true, this).setVisibility(v-> animationSubBool.getValue());
        yl = new Setting<>("YLA", true, this).setVisibility(v-> animationSubBool.getValue());
        zl = new Setting<>("ZLA", true, this).setVisibility(v-> animationSubBool.getValue());

        swingSpeed = new Setting<>("SwingSpeed", 20, this, 1, 40);
    }


    Timer rtx = new Timer();
    Timer rty = new Timer();
    Timer rtz = new Timer();
    Timer ltx = new Timer();
    Timer lty = new Timer();
    Timer ltz = new Timer();
    @Override
    public void onToggle() {
        super.onToggle();
        rtx.reset();
        rty.reset();
        rtz.reset();
        ltx.reset();
        lty.reset();
        ltz.reset();
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(e-> {

        if (xr.getValue()) {
            if (rtx.hasPassedMs(0)) {
                if (RRX.getValue() < 360) {
                    RRX.setValue(RRX.getValue()+addAmount.getValue());
                    rtx.reset();
                } else if (RRX.getValue() >= 360) {
                    RRX.setValue(-360);
                }
            }
        }

        if (yr.getValue()) {
            if (rty.hasPassedMs(0)) {
                if (RRY.getValue() < 360) {
                    RRY.setValue(RRY.getValue()+addAmount.getValue());
                    rty.reset();
                } else if (RRY.getValue() >= 360) {
                    RRY.setValue(-360);
                }
            }
        }

        if (zr.getValue()) {
            if (rtz.hasPassedMs(0)) {
                if (RRZ.getValue() < 360) {
                    RRZ.setValue(RRZ.getValue()+addAmount.getValue());
                    rtz.reset();
                } else if (RRZ.getValue() >= 360) {
                    RRZ.setValue(-360);
                }
            }
        }

        if (xl.getValue()) {
            if (ltx.hasPassedMs(0)) {
                if (RLX.getValue() < 360) {
                    RLX.setValue(RLX.getValue()+addAmount.getValue());
                    ltx.reset();
                } else if (RLX.getValue() >= 360) {
                    RLX.setValue(-360);
                }
            }
        }

        if (yl.getValue()) {
            if (lty.hasPassedMs(0)) {
                if (RLY.getValue() < 360) {
                    RLY.setValue(RLY.getValue()+addAmount.getValue());
                    lty.reset();
                } else if (RLY.getValue() >= 360) {
                    RLY.setValue(-360);
                }
            }
        }

        if (zl.getValue()) {
            if (ltz.hasPassedMs(0)) {
                if (RLZ.getValue() < 360) {
                    RLZ.setValue(RLZ.getValue()+addAmount.getValue());
                    ltz.reset();
                } else if (RLZ.getValue() >= 360) {
                    RLZ.setValue(-360);
                }
            }
        }

    });
}
