package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Gui.Gui;
import net.minecraft.client.Minecraft;

import java.util.concurrent.TimeUnit;

@CommandManifest(label = "gui")
public class GuiCommand extends Command {
    @Override
    public void execute(String[] args) {
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
