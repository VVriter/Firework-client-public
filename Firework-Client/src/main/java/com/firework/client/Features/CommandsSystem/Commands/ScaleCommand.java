package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Implementations.Gui.GuiInfo;

@CommandManifest(label = "scale")
public class ScaleCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();
        super.execute(args);
        GuiInfo.guiScale = Double.parseDouble(args[1]);
    }
}
