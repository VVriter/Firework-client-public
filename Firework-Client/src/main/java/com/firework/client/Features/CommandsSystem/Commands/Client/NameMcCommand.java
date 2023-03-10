package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@CommandManifest(label = "namemc",aliases = "profile")
public class NameMcCommand extends Command {
    @Override
    public void execute(String[] args) {

        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("https://namemc.com/profile/" + URLEncoder.encode(args[1])));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
