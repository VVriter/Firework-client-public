package com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.Util.ApiRequester;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;

import java.io.IOException;

@CommandManifest(label = "queue")
public class QueueCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
        MessageUtil.sendClientMessage("Players in queue: "+ApiRequester.getPrioQueueLength(),false);
        MessageUtil.sendClientMessage("Estimated time: "+ApiRequester.getPrioTime(),false);}catch (Exception e){

                        }
                    }
                }).start();
    }
}
