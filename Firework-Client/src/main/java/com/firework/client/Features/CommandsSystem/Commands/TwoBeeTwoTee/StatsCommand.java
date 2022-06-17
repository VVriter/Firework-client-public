package com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.Util.ApiRequester;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.Util.NetworkUtil;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;

import java.io.IOException;

@CommandManifest(label = "stats")
public class StatsCommand extends Command {
    @Override
    public void execute(String[] args) {
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
        //Plays Notification sound
        Notifications.notificate();
        try{
            String text = NetworkUtil.makeRequest("https://api.2b2t.dev/stats?username=" + args[1]);
            if (text == null || text.isEmpty() || text.equals("[]")) {
                MessageUtil.sendError("An exception occurred when retrieving the date, or this player has not been seen.",-1117);
            }
            JsonArray array = new Gson().fromJson(text, JsonArray.class);
            if (array.size() == 0) {
                MessageUtil.sendError("This player has not been seen.",-1117);
            }
            JsonObject data = array.get(0).getAsJsonObject();

            MessageUtil.sendClientMessage(ChatFormatting.GREEN +"--------"+args[1]+" STATS"+"--------",false);
            MessageUtil.sendClientMessage(ChatFormatting.GREEN+"Kills: "+data.get("kills").getAsString(),false);
            MessageUtil.sendClientMessage(ChatFormatting.GREEN+"Deaths: "+data.get("deaths").getAsString(),false);
            MessageUtil.sendClientMessage(ChatFormatting.GREEN+"Joins: "+data.get("joins").getAsString(),false);
            MessageUtil.sendClientMessage(ChatFormatting.GREEN+"Leaves: "+data.get("leaves").getAsString(),false);


        }catch (IndexOutOfBoundsException e){
            MessageUtil.sendError("This player has not been seen.",-1117);
        }catch (IllegalArgumentException ex){
            MessageUtil.sendError("This player has not been seen.",-1117);
        }}catch (Exception e){

                        }
                    }
                }).start();
    }
}