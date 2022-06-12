package com.firework.client.Features.CommandsSystem.Commands.MuteSystem;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Managers.MuteManager;

@CommandManifest(label = "bebro")
public class GetList extends Command {
    @Override
    public void execute(String[] args) {
        MuteManager.getListOfNamesOfMutedPlayers();
    }
}
