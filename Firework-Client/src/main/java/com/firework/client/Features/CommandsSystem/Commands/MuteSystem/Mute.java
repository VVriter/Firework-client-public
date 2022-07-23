package com.firework.client.Features.CommandsSystem.Commands.MuteSystem;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Managers.MuteManager;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

@CommandManifest(label = "mute")
public class Mute extends Command {
    @Override
    public void execute(String[] args) {
        MuteManager.addMuted(args[1]);
        MessageUtil.sendClientMessage(args[1]+" is muted now, use "+ CommandManager.prefix+"unmute command to unmute!",-1117);
    }
}