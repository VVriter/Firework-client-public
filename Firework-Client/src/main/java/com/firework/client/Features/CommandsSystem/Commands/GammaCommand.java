package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.event.ClickEvent;


@CommandManifest(label = "gamma",aliases = "gm")
public class GammaCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();
        try {
            Firework.minecraft.gameSettings.gammaSetting = Float.parseFloat(args[1]);
            MessageUtil.sendClientMessage("Gamma is setted to: "+args[1],-11114);}catch (NumberFormatException e){
            MessageUtil.sendError("Only Numbers LoL",-11114);
        }
    }
}


