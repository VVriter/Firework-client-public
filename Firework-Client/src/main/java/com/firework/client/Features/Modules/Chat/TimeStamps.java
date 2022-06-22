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

    public Setting<String> bracketColor = new Setting<>("BracketColor", "WHITE", this, Arrays.asList("WHITE","BLACK", "DARK_BLUE","DARK_GREEN","DARK_AQUA"
            ,"DARK_RED","DARK_PURPLE","GOLD","GRAY","DARK_GRAY",
            "BLUE","GREEN","AQUA","RED","LIGHT_PURPLE","YELLOW"));
    public Setting<String> timeColor = new Setting<>("TimeColor", "WHITE", this, Arrays.asList("WHITE","BLACK", "DARK_BLUE","DARK_GREEN","DARK_AQUA"
            ,"DARK_RED","DARK_PURPLE","GOLD","GRAY","DARK_GRAY",
            "BLUE","GREEN","AQUA","RED","LIGHT_PURPLE","YELLOW"));


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
        if(bracketColor.getValue().equals("WHITE")){
            bracketFormatting = ChatFormatting.RESET;
        }if(bracketColor.getValue().equals("BLACK")){
            bracketFormatting = ChatFormatting.BLACK;
        }if(bracketColor.getValue().equals("DARK_BLUE")){
            bracketFormatting = ChatFormatting.DARK_BLUE;
        }if(bracketColor.getValue().equals("DARK_GREEN")){
            bracketFormatting = ChatFormatting.DARK_GREEN;
        }if(bracketColor.getValue().equals("DARK_AQUA")){
            bracketFormatting = ChatFormatting.DARK_AQUA;
        }if(bracketColor.getValue().equals("DARK_RED")){
            bracketFormatting = ChatFormatting.DARK_RED;
        }if(bracketColor.getValue().equals("DARK_PURPLE")){
            bracketFormatting = ChatFormatting.DARK_PURPLE;
        }if(bracketColor.getValue().equals("GOLD")){
            bracketFormatting = ChatFormatting.GOLD;
        }if(bracketColor.getValue().equals("GRAY")){
            bracketFormatting = ChatFormatting.GRAY;
        }if(bracketColor.getValue().equals("DARK_GRAY")){
            bracketFormatting = ChatFormatting.DARK_GRAY;
        }if(bracketColor.getValue().equals("BLUE")){
            bracketFormatting = ChatFormatting.BLUE;
        }if(bracketColor.getValue().equals("GREEN")){
            bracketFormatting = ChatFormatting.GREEN;
        }if(bracketColor.getValue().equals("AQUA")){
            bracketFormatting = ChatFormatting.AQUA;
        }if(bracketColor.getValue().equals("RED")){
            bracketFormatting = ChatFormatting.RED;
        }if(bracketColor.getValue().equals("LIGHT_PURPLE")){
            bracketFormatting = ChatFormatting.LIGHT_PURPLE;
        }if(bracketColor.getValue().equals("YELLOW")) {
            bracketFormatting = ChatFormatting.YELLOW;
        }


        //Time Formatting
        if(timeColor.getValue().equals("WHITE")){
            timeFormatting = ChatFormatting.RESET;
        }if(timeColor.getValue().equals("BLACK")){
            timeFormatting = ChatFormatting.BLACK;
        }if(timeColor.getValue().equals("DARK_BLUE")){
            timeFormatting = ChatFormatting.DARK_BLUE;
        }if(timeColor.getValue().equals("DARK_GREEN")){
            timeFormatting = ChatFormatting.DARK_GREEN;
        }if(timeColor.getValue().equals("DARK_AQUA")){
            timeFormatting = ChatFormatting.DARK_AQUA;
        }if(timeColor.getValue().equals("DARK_RED")){
            timeFormatting = ChatFormatting.DARK_RED;
        }if(timeColor.getValue().equals("DARK_PURPLE")){
            timeFormatting = ChatFormatting.DARK_PURPLE;
        }if(timeColor.getValue().equals("GOLD")){
            timeFormatting = ChatFormatting.GOLD;
        }if(timeColor.getValue().equals("GRAY")){
            timeFormatting = ChatFormatting.GRAY;
        }if(timeColor.getValue().equals("DARK_GRAY")){
            timeFormatting = ChatFormatting.DARK_GRAY;
        }if(timeColor.getValue().equals("BLUE")){
            timeFormatting = ChatFormatting.BLUE;
        }if(timeColor.getValue().equals("GREEN")){
            timeFormatting = ChatFormatting.GREEN;
        }if(timeColor.getValue().equals("AQUA")){
            timeFormatting = ChatFormatting.AQUA;
        }if(timeColor.getValue().equals("RED")){
            timeFormatting = ChatFormatting.RED;
        }if(timeColor.getValue().equals("LIGHT_PURPLE")){
            timeFormatting = ChatFormatting.LIGHT_PURPLE;
        }if(timeColor.getValue().equals("YELLOW")) {
            timeFormatting = ChatFormatting.YELLOW;
        }

    }


    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String strDate = dateFormatter.format(date);
        TextComponentString time = new TextComponentString(bracketFormatting + firstBracket + ChatFormatting.RESET + timeFormatting + strDate + ChatFormatting.RESET +bracketFormatting+lastBracket + " ");
        event.setMessage(time.appendSibling(event.getMessage()));
    }
}