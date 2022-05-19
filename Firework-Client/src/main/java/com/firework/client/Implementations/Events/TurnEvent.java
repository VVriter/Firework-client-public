package com.firework.client.Implementations.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class TurnEvent extends CancellableEvent{
    private final float yaw;
    private final float pitch;

    public TurnEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}