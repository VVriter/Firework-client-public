package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Commands.Command;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Managers.Module.ModuleManager;

public class CDiscordServerOpener extends Command {

    public CDiscordServerOpener() {
        super(ModuleManager.discordServerOpener, "discord");
    }
}
