package com.firework.client.Features.CommandsSystem.Commands.FriendSys;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Managers.FriendManager;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;

@CommandManifest(label = "addfriend")
public class FriendAdd extends Command {
    @Override
    public void execute(String[] args) {
        FriendManager.parse(args[1]);
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.sendChatMessage("/w "+args[1]+" You are added as friend [FIREWORK CLIENT]");
        MessageUtil.sendClientMessage(args[1]+" added as friend!",false);
    }
}
