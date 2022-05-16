package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Gui.Gui;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@CommandManifest(label = "gui",aliases = "g")
public class GuiCommand extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft.getMinecraft().displayGuiScreen(new Gui());
    }
}
