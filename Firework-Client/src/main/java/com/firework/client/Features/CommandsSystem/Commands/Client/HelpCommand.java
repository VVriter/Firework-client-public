package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

@CommandManifest(label = "help",aliases = "cord")
public class HelpCommand extends Command {
    @Override
    public void execute(String[] args) {
        MessageUtil.sendShown("Help Command - Usage: "+prefix+"help ","Sends list of commands",false);
        MessageUtil.sendShown("Clear Command - Usage: "+prefix+"clear ","Clears chat",false);
        MessageUtil.sendShown("Coords Command - Usage: "+prefix+"coords ","Copies ur coords to clipboard",false);
        MessageUtil.sendShown("Coords Command - Usage: "+prefix+"coords ","hanges your fov",false);
        MessageUtil.sendShown("NameMc Command - Usage: "+prefix+"namemc nickoftheplayer ","Open ncmemc site with profile",false);
        MessageUtil.sendShown("Webhook Command - Usage: "+prefix+"webhook urDiscordWebhook ","Adds ur webhook",false);
        MessageUtil.sendShown("Dupe Command - Usage: "+prefix+"dupe ","Makes an a sexdupe",false);
        MessageUtil.sendShown("Gamma Command - Usage: "+prefix+"gamma floatvalue ","Changes gamma setting",false);
        MessageUtil.sendShown("Yaw Command - Usage: "+prefix+"Yaw floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("Pitch Command - Usage: "+prefix+"Pitch floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("VClip Command - Usage: "+prefix+"Clip floatvalue ","Moves ur up",false);
    }
}