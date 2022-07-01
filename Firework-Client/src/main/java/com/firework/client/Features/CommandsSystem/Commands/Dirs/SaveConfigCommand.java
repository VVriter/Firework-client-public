package com.firework.client.Features.CommandsSystem.Commands.Dirs;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Features.Modules.ModuleManager;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@CommandManifest(label = "save")
public class SaveConfigCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();
        //Firework.moduleManager.saveModules();
        MessageUtil.sendClientMessage("ModulesSaved!",true);
    }
}