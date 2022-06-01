package com.firework.client.Features.Modules.Client;

import com.firework.client.CustomDiscordRichPresence;
import com.firework.client.Features.Modules.Module;

public class DiscordRPCModule extends Module {

    public DiscordRPCModule() {
        super("DiscordRPC", Category.CLIENT);
    }

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
