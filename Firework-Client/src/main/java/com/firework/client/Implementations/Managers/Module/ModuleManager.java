package com.firework.client.Implementations.Managers.Module;

import com.firework.client.Features.Commands.Client.DiscordServerOpener;
import com.firework.client.Features.Modules.Client.CDiscordServerOpener;
import com.firework.client.Features.Modules.Module;

import java.util.ArrayList;

public class ModuleManager {
    public static DiscordServerOpener discordServerOpener;

    public static CDiscordServerOpener discordServerOpenerCommand;

    public ModuleManager(){
        registerModules();
    }

    public void registerModules(){
        discordServerOpener = new DiscordServerOpener();
        discordServerOpenerCommand = new CDiscordServerOpener();
    }
}
