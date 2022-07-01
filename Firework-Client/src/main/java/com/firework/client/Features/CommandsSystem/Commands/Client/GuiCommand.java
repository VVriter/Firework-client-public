package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import net.minecraft.client.Minecraft;

@CommandManifest(label = "gui")
public class GuiCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();
        Minecraft.getMinecraft().player.closeScreen();
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            Minecraft.getMinecraft().displayGuiScreen(new Gui());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }
}
