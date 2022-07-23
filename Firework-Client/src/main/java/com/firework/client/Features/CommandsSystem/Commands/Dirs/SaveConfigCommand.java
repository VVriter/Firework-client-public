package com.firework.client.Features.CommandsSystem.Commands.Dirs;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

@CommandManifest(label = "save")
public class SaveConfigCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Firework.moduleManager.saveModules();
        MessageUtil.sendClientMessage("ModulesSaved!",true);
    }
}