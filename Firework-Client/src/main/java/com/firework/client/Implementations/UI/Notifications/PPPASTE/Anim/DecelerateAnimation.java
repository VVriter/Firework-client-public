package com.firework.client.Implementations.UI.Notifications.PPPASTE.Anim;

import com.firework.client.Implementations.UI.Notifications.PPPASTE.Animation;
import com.firework.client.Implementations.UI.Notifications.PPPASTE.Direction;

public class DecelerateAnimation extends Animation {

    public DecelerateAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DecelerateAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x) {
        double x1 = x / duration;
        return 1 - ((x1 - 1) * (x1 - 1));
    }
}