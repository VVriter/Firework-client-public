package com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.Util.NetworkUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@CommandManifest(label = "seen")
public class SeenCommand extends Command {
    @Override
    public void execute(String[] args) {
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
        try{
        String text = NetworkUtil.makeRequest("https://api.2b2t.dev/seen?username=" + args[1]);
        if (text == null || text.isEmpty() || text.equals("[]")) {
            MessageUtil.sendError("An exception occurred when retrieving the date, or this player has not been seen.",-1117);
        }
        JsonArray array = new Gson().fromJson(text, JsonArray.class);
        if (array.size() == 0) {
            MessageUtil.sendError("This player has not been seen.",-1117);
        }
        JsonObject data = array.get(0).getAsJsonObject();

        MessageUtil.sendClientMessage(args[1] + " has been last seen at " + data.get("seen").getAsString(),-1117);
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