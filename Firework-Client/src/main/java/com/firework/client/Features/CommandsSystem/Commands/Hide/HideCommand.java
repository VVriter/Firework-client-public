package com.firework.client.Features.CommandsSystem.Commands.Hide;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;


@CommandManifest(label = "hide")
public class HideCommand extends Command {
    @Override
    public void execute(String[] args) {
       // ArrayListHud.names.remove(args[1]);
        MessageUtil.sendClientMessage("Now hiding "+args[1]+"module",false);
    }
}
