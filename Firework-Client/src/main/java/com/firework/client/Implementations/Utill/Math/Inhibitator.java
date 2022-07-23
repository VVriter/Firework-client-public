package com.firework.client.Implementations.Utill.Math;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;

public class Inhibitator {

    public  boolean shuldMoveRight = true;
    public  boolean shuldMoveLeft = false;

    public Timer timer = new Timer();

    public void doInhibitation(Setting<Double> settingToInhibit, double inhibitationSpeed, double reachToFirst, double reachToSecond) {

        if (settingToInhibit.getValue() >= reachToFirst) {
            returner();
        }

        if (settingToInhibit.getValue() == reachToSecond) {
            returner2();
        }

            if (shuldMoveRight) {
                if (timer.hasPassedMs(inhibitationSpeed)) {
                    settingToInhibit.setValue((double) Math.round(settingToInhibit.getValue()+1));
                    timer.reset();
                }
            }

            if (shuldMoveLeft) {
                if (timer.hasPassedMs(inhibitationSpeed)) {
                    settingToInhibit.setValue((double) Math.round(settingToInhibit.getValue() - 1));
                    timer.reset();
                }
            }

    }

    public void returner() {
            shuldMoveRight = false;
            shuldMoveLeft = true;
    }

    public void returner2() {
            shuldMoveRight = true;
            shuldMoveLeft = false;
    }
}
