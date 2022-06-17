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

@CommandManifest(label = "help3")
public class Help3Command extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        MessageUtil.sendClientMessage(ChatFormatting.DARK_RED + "PAGE: 3\n",-1117);


        MessageUtil.sendShown("Yaw Command - Usage: "+CommandManager.prefix+"Yaw floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("Pitch Command - Usage: "+CommandManager.prefix+"Pitch floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("VClip Command - Usage: "+CommandManager.prefix+"Clip floatvalue ","Moves ur up",false);
        MessageUtil.sendShown("Peek Command - Usage: "+CommandManager.prefix+"peek ","U need to hold shulker in main hand",false);
        MessageUtil.sendShown("Penis Command - Usage: "+CommandManager.prefix+"penis \n","Show ur penis size :)",false);


        ITextComponent msg1 = new TextComponentString(ChatFormatting.RED + "[CLICKABLE] Next Page >>>")
                .setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        CommandManager.prefix+"help4")));


        ITextComponent msg2 = new TextComponentString(ChatFormatting.RED + "[CLICKABLE] <<< Previous Page");
        msg2.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,CommandManager.prefix+"help2")));



        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(msg1);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(msg2);
    }
}
