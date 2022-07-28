package com.firework.client.Features.Modules.Client;

import com.firework.client.CustomDiscordRichPresence;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;

@ModuleManifest(
        name = "Discord RPC",
        category = Module.Category.CLIENT,
        description = "Cool discord RPC"
)
public class DiscordRPCModule extends Module {
    @Override
    public void onEnable() {
        super.onEnable();

        new Thread(() -> {
            CustomDiscordRichPresence.run();
        }).start();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        CustomDiscordRichPresence.stop();
    }
}
