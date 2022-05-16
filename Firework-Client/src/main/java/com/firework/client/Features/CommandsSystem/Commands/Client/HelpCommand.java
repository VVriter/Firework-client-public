package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;

@CommandManifest(label = "help",aliases = "cord")
public class HelpCommand extends Command {
    @Override
    public void execute(String[] args) {
        MessageUtil.sendClientMessage("Help Command - Usage: "+prefix+"help "+ChatFormatting.RESET+ChatFormatting.DARK_PURPLE+"(Sends list of commands)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"Clear Command - Usage: "+prefix+"clear "+ ChatFormatting.RESET+ChatFormatting.DARK_PURPLE+"(Clears chat)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"Coords Command - Usage: "+prefix+"coords "+ChatFormatting.RESET+ ChatFormatting.DARK_PURPLE+"(Copies ur coords to clipboard)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"Fov Command - Usage: "+prefix+"fov 120 "+ChatFormatting.RESET+ ChatFormatting.DARK_PURPLE+"(Changes your fov)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"NameMc Command - Usage: "+prefix+"namemc nickoftheplayer "+ChatFormatting.RESET+ ChatFormatting.DARK_PURPLE+"(Open ncmemc site with profile)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"Tutorial Command - Usage: "+prefix+"tut "+ChatFormatting.RESET+ ChatFormatting.DARK_PURPLE+"(Clears your tutorial steps)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"Webhook Command - Usage: "+prefix+"webhook urDiscordWebhook "+ChatFormatting.RESET+ ChatFormatting.DARK_PURPLE+"(Adds ur webhook)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"Dupe Command - Usage: "+prefix+"dupe "+ChatFormatting.RESET+ ChatFormatting.DARK_PURPLE+"(Makes an a sexdupe)"+ChatFormatting.RESET+"\n"
                +ChatFormatting.GRAY+"Gamma Command - Usage: "+prefix+"gamma floatvalue "+ChatFormatting.RESET+ ChatFormatting.DARK_PURPLE+"(Changes gamma setting)"+ChatFormatting.RESET+"\n",-11114);
    }
}