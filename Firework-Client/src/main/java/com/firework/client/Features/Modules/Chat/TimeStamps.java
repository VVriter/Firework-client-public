package com.firework.client.Features.Modules.Chat;

/** @Author dazed68
 * Advenced timestaps owncoded!
 * Like in RUSHERHACK CLIENT
 * im sooo good!*/


import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@ModuleManifest(name = "TimeStamps",category = Module.Category.CHAT)
public class TimeStamps extends Module {

    public Setting<String> BracketMode = new Setting<>("Brackets", "Bracket", this, Arrays.asList("Bracket","Curly", "Parent"));
    public Setting<String> Format = new Setting<>("Format", "European", this, Arrays.asList("European","AM/PM"));

    public Setting<ChatFormatting> bracketColor = new Setting<>("BracketColor",  ChatFormatting.WHITE, this, ChatFormatting.values());
    public Setting<ChatFormatting> timeColor = new Setting<>("TimeColor", ChatFormatting.WHITE, this, ChatFormatting.values());

    String firstBracket; //= new TextComponentString("<");
    String lastBracket; // = new TextComponentString(">");

    SimpleDateFormat dateFormatter;

    Date date = new Date();

    ChatFormatting bracketFormatting;
    ChatFormatting timeFormatting;

    @Override
    public void onTick(){
        super.onTick();
        if(BracketMode.getValue().equals("Bracket")){
            firstBracket ="[";
            lastBracket ="]";
        }if(BracketMode.getValue().equals("Curly")){
            firstBracket = "<";
            lastBracket = ">";
        }if(BracketMode.getValue().equals("Parent")){
            firstBracket = "(";
            lastBracket = ")";
        }

        if(Format.getValue().equals("European")){
            dateFormatter = new SimpleDateFormat("HH:mm:ss");
        }else if(Format.getValue().equals("AM/PM")){
            dateFormatter = new SimpleDateFormat("h:mm a");
        }


        //Bracket Formatting
        bracketFormatting = bracketColor.getValue();

        //Time Formatting
        timeFormatting = timeColor.getValue();
    }


    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String strDate = dateFormatter.format(date);
        TextComponentString time = new TextComponentString(bracketFormatting + firstBracket + ChatFormatting.RESET + timeFormatting + strDate + ChatFormatting.RESET +bracketFormatting+lastBracket + " ");
        event.setMessage(time.appendSibling(event.getMessage()));
    }
}