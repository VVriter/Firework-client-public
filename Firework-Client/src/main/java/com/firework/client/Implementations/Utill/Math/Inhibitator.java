package com.firework.client.Implementations.Utill.Math;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;

public class Inhibitator {

    public static boolean shuldMoveRight = true;
    public static boolean shuldMoveLeft = false;

    public static Timer moveRightTimer = new Timer();
    public static Timer moveLeftTimer = new Timer();

    public static void doInhibitation(Setting<Double> settingToInhibit, double inhibitationSpeed, double reachToFirst, double reachToSecond) {

        if (settingToInhibit.getValue() >= reachToFirst) {
            returner();
        }

        if (settingToInhibit.getValue() == reachToSecond) {
            returner2();
        }

            if (shuldMoveRight) {
                if (moveRightTimer.hasPassedMs(inhibitationSpeed)) {
                    settingToInhibit.setValue((double) Math.round(settingToInhibit.getValue()+1));
                    moveRightTimer.reset();
                }
            }

            if (shuldMoveLeft) {
                if (moveLeftTimer.hasPassedMs(inhibitationSpeed)) {
                    settingToInhibit.setValue((double) Math.round(settingToInhibit.getValue() - 1));
                    moveLeftTimer.reset();
                }
            }

    }

    public static void returner() {
            shuldMoveRight = false;
            shuldMoveLeft = true;
    }

    public static void returner2() {
            shuldMoveRight = true;
            shuldMoveLeft = false;
    }
}
