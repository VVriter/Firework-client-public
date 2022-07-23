package com.firework.client.Features.CommandsSystem.Commands.Dirs;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@CommandManifest(label = "opendir",aliases = "dir")
public class OpenDirCommand extends Command {
    @Override
    public void execute(String[] args) {
        try {
            Desktop.getDesktop().open(new File(Firework.FIREWORK_DIRECTORY));
            MessageUtil.sendClientMessage("Opening firework dir",true);
        } catch (IOException e) {
            MessageUtil.sendError("Error while openning dir :(",-1117);
        }
    }
}
