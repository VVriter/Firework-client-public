package com.firework.client.Features.CommandsSystem.Commands.MuteSystem;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Implementations.Managers.MuteManager;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

@CommandManifest(label = "unmute")
public class UnMute extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();
        MuteManager.removeFromMuteList(args[1]);
        MessageUtil.sendClientMessage(args[1]+"removed from mute list now, use "+ CommandManager.prefix+"mute to mute!",-1117);
    }
}