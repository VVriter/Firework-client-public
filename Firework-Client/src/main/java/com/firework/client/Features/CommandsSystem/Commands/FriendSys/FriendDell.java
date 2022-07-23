package com.firework.client.Features.CommandsSystem.Commands.FriendSys;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Managers.FriendManager;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

@CommandManifest(label = "dellfriend")
public class FriendDell extends Command {
    @Override
    public void execute(String[] args) {
        FriendManager.remove(args[1]);
        MessageUtil.sendClientMessage(args[1]+" is deleted as friend!",false);
    }
}
