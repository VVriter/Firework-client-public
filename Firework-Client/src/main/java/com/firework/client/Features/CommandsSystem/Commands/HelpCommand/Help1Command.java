package com.firework.client.Features.CommandsSystem.Commands.HelpCommand;

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

@CommandManifest(label = "help")
public class Help1Command extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        MessageUtil.sendClientMessage(ChatFormatting.DARK_RED + "PAGE: 1\n",-1117);
        MessageUtil.sendShown("Help Command - Usage: "+ CommandManager.prefix +"help ","Sends list of commands",false);
        MessageUtil.sendShown("Fakeplayer Command - Usage: "+ CommandManager.prefix +"fakeplayer nickname ","Spawns fakeplayer",false);
        MessageUtil.sendShown("Dir Command - Usage: "+ CommandManager.prefix +"dir ","Opens firework dir",false);
        MessageUtil.sendShown("Clear Command - Usage: "+CommandManager.prefix+"clear ","Clears chat",false);
        MessageUtil.sendShown("Coords Command - Usage: "+CommandManager.prefix+"coords \n","Copies ur coords to clipboard",false);

        ITextComponent msg1 = new TextComponentString(ChatFormatting.RED + "[CLICKABLE] Next Page >>>");
        msg1.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,CommandManager.prefix+"help2")));

        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(msg1);
    }
}
