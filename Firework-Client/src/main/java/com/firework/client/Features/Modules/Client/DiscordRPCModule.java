package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.FireworkDiscordRPC;

public class DiscordRPCModule extends Module {

    public DiscordRPCModule() {
        super("DiscordRPC", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        FireworkDiscordRPC.run();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        FireworkDiscordRPC.stop();
    }
}
