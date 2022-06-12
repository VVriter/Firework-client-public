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

@CommandManifest(label = "help4")
public class Help4Command extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        MessageUtil.sendClientMessage(ChatFormatting.DARK_RED + "PAGE: 4\n",-1117);

        MessageUtil.sendShown("Book Command - Usage: "+CommandManager.prefix+"book ","Makes an a dupe book",false);
        MessageUtil.sendShown("Prefix Command - Usage: "+CommandManager.prefix+"prefix value ","Changes ur command prefix!",false);
        MessageUtil.sendShown("CowDupe Command - Usage: "+CommandManager.prefix+"cowdupe \n","DupesCows!",false);
        MessageUtil.sendShown("Mute Command - Usage: "+CommandManager.prefix+"mute name \n","Muting player from chat client side only",false);
        MessageUtil.sendShown("UnMute Command - Usage: "+CommandManager.prefix+"unmute name \n","Unmuting player from chat",false);





        ITextComponent msg2 = new TextComponentString(ChatFormatting.RED + "[CLICKABLE] <<< Previous Page");
        msg2.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,CommandManager.prefix+"help3")));


        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(msg2);
    }
}