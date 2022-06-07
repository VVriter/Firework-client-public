package com.firework.client.Features.CommandsSystem.Commands.HelpCommand;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;

@CommandManifest(label = "help3")
public class Help3Command extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        MessageUtil.sendClientMessage(ChatFormatting.DARK_RED + "PAGE: 3",-1117);
        MessageUtil.sendShown("Yaw Command - Usage: "+CommandManager.prefix+"Yaw floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("Pitch Command - Usage: "+CommandManager.prefix+"Pitch floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("VClip Command - Usage: "+CommandManager.prefix+"Clip floatvalue ","Moves ur up",false);
        MessageUtil.sendShown("Peek Command - Usage: "+CommandManager.prefix+"peek ","U need to hold shulker in main hand",false);
        MessageUtil.sendShown("Penis Command - Usage: "+CommandManager.prefix+"penis ","Show ur penis size :)",false);
        MessageUtil.sendClickable( "NEXT PAGE >>>>",CommandManager.prefix+"help4",false);
        MessageUtil.sendClickable( "<<<<< PREVIOVUS PAGE",CommandManager.prefix+"help2",false);
    }
}
