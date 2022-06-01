package com.firework.client.Features.Modules.Client;

import com.firework.client.DiscordRichPresence;
import com.firework.client.Features.Modules.Module;

public class DiscordRPCModule extends Module {

    public DiscordRPCModule() {
        super("DiscordRPC", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        new Thread(() -> {
            DiscordRichPresence.run();
        }).start();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        DiscordRichPresence.stop();
    }
}
