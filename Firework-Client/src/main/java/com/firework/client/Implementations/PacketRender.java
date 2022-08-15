package com.firework.client.Implementations;

import com.firework.client.Firework;

public class PacketRender {
    private static float yaw = 0;
    private static float pitch = 0;

    public static float getYaw() {
        return yaw;
    }

    public static float getPitch() {
        return pitch;
    }

    public static void setYaw(float yaw) {
        Firework.packetRender.yaw = yaw;
    }

    public static void setPitch(float pitch) {
        Firework.packetRender.pitch = pitch;
    }
}
