package com.firework.client.Features.CommandsSystem.Commands.HelpCommand;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;

@CommandManifest(label = "help4")
public class Help4Command extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        MessageUtil.sendClientMessage(ChatFormatting.DARK_RED + "PAGE: 4",-1117);
        MessageUtil.sendShown("Book Command - Usage: "+CommandManager.prefix+"book ","Makes an a dupe book",false);
        MessageUtil.sendShown("Prefix Command - Usage: "+CommandManager.prefix+"prefix value ","Changes ur command prefix!",false);
        MessageUtil.sendShown("CowDupe Command - Usage: "+CommandManager.prefix+"cowdupe ","DupesCows!",false);
        MessageUtil.sendClickable( "<<<<< PREVIOVUS PAGE",CommandManager.prefix+"help3",false);
    }
}