package com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.HelpCommand;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

@CommandManifest(label = "2b2t")
public class HelpCommand extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        MessageUtil.sendShown("2b2t Command - Usage: "+ CommandManager.prefix +"2b2t ","Sends this page",false);
        MessageUtil.sendShown("Queue Command - Usage: "+ CommandManager.prefix +"queue ","Sends about how many players in priority queue now",false);
        MessageUtil.sendShown("Seen Command - Usage: "+ CommandManager.prefix +"seen ","Sends info about what time player joined 2b2t",false);
        MessageUtil.sendShown("Stats Command - Usage: "+CommandManager.prefix+"stats ","Sends stats about player on the 2b2t server",false);
    }
}