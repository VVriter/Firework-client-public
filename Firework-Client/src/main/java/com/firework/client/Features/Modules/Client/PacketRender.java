package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "PacketRender",
        category = Module.Category.CLIENT,
        description = ""
)
public class PacketRender extends Module {
    private static float yaw = 0;
    private static float pitch = 0;
    public static Setting<Boolean> enabled = null;
    public PacketRender() {
        enabled = this.isEnabled;
    }

    @Subscribe
    public Listener<PacketEvent.Send> listener = new Listener<>(e-> {

    });

    public static float getYaw() {
        return yaw;
    }

    public static float getPitch() {
        return pitch;
    }

    public static void setYaw(float yaw) {
        PacketRender.yaw = yaw;
    }

    public static void setPitch(float pitch) {
        PacketRender.pitch = pitch;
    }
}
