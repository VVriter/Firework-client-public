package com.firework.client.Features.CommandsSystem.Commands.MuteSystem;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Firework;
import net.minecraft.client.Minecraft;

@CommandManifest(label = "mute")
public class Mute extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();

    }
}