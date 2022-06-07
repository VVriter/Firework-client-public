package com.firework.client.Features.CommandsSystem.Commands.HelpCommand;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;

@CommandManifest(label = "help2")
public class Help2Command extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        MessageUtil.sendClientMessage(ChatFormatting.DARK_RED + "PAGE: 2",-1117);
        MessageUtil.sendShown("Fov Command - Usage: "+CommandManager.prefix+"fov ","Changes your fov",false);
        MessageUtil.sendShown("NameMc Command - Usage: "+CommandManager.prefix+"namemc nickoftheplayer ","Open ncmemc site with profile",false);
        MessageUtil.sendShown("Webhook Command - Usage: "+CommandManager.prefix+"webhook urDiscordWebhook ","Adds ur webhook",false);
        MessageUtil.sendShown("Dupe Command - Usage: "+CommandManager.prefix+"dupe ","Makes an a sexdupe",false);
        MessageUtil.sendShown("Gamma Command - Usage: "+CommandManager.prefix+"gamma floatvalue ","Changes gamma setting",false);
        MessageUtil.sendClickable( "NEXT PAGE >>>>",CommandManager.prefix+"help3",false);
        MessageUtil.sendClickable( "<<<<< PREVIOVUS PAGE",CommandManager.prefix+"help",false);
    }
}
