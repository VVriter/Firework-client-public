package com.firework.client.Features.Commands.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;

public class DiscordServerOpener extends Module {

    public DiscordServerOpener() {
        super("DiscordServerOpener", Category.CLIENT, true);
    }

    @Override
    public void execute() {
        DiscordUtil.OpenServer();
    }
}
